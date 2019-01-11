package com.linus.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.hankcs.hanlp.utility.Predefine;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author yuxuecheng
 * @Title: HanLPTest
 * @ProjectName java_git
 * @Description: 测试HanLP中文分词工具
 * @date 2019-01-09 17:51
 */
public class HanLPTest {

    private static void testPerceptronLexicalAnalyzer() {
        try {
            File file = new File("data/model/perceptron/pku199801/cws.bin");
            if (file.exists()) {
                System.out.println("文件存在");
                System.out.println(file.getAbsolutePath());
            } else {
                System.out.println("文件不存在");
            }
            PerceptronLexicalAnalyzer analyzer = new PerceptronLexicalAnalyzer(HanLP.Config.PerceptronCWSModelPath,
                    HanLP.Config.PerceptronPOSModelPath,
                    HanLP.Config.PerceptronNERModelPath);
            System.out.println(analyzer.analyze("上海华安工业（集团）公司董事长谭旭光和秘书胡花蕊来到美国纽约现代艺术博物馆参观"));
            System.out.println(analyzer.analyze("微软公司於1975年由比爾·蓋茲和保羅·艾倫創立，18年啟動以智慧雲端、前端為導向的大改組。"));

            // 任何模型总会有失误，特别是98年这种陈旧的语料库
            System.out.println(analyzer.analyze("总统普京与特朗普通电话讨论太空探索技术公司"));
            // 支持在线学习
            analyzer.learn("与/c 特朗普/nr 通/v 电话/n 讨论/v [太空/s 探索/vn 技术/n 公司/n]/nt");
            // 学习到新知识
            System.out.println(analyzer.analyze("总统普京与特朗普通电话讨论太空探索技术公司"));
            // 还可以举一反三
            System.out.println(analyzer.analyze("主席和特朗普通电话"));

            // 知识的泛化不是死板的规则，而是比较灵活的统计信息
            System.out.println(analyzer.analyze("我在浙江金华出生"));
            analyzer.learn("在/p 浙江/ns 金华/ns 出生/v");
            System.out.println(analyzer.analyze("我在四川金华出生，我的名字叫金华"));

            // 在线学习后的模型支持序列化，以分词模型为例：
            analyzer.getPerceptronSegmenter().getModel().save(HanLP.Config.PerceptronCWSModelPath);

            /*
             * 请用户按需执行对空格制表符等的预处理，只有你最清楚自己的文本中都有些什么奇怪的东西
             * 去除所有空白符
             * 如果一些文本中含有html控制符
             */
            System.out.println(analyzer.analyze("空格 \t\n\r\f&nbsp;统统都不要"
                    .replaceAll("\\s+", "")
                    .replaceAll("&nbsp;", "")
            ));
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    private static void testStandardTokenizer() {
        System.out.println(HanLP.segment("你好，欢迎使用HanLP汉语处理包！"));

        // 标准分词
        List<Term> termList = StandardTokenizer.segment("商品和服务");
        System.out.println(termList);
        String str = "入院诊断：患者于三年前腹痛\n" +
                "入院情况：\n" +
                "患者因“诊断尿毒症4年余，反复心悸1天”入院，体检：T 36.3℃ P 94次/分 R 19次/分 BP 132/95mmHg；慢性肾病面容，贫血貌，全身皮肤黏膜无黄染及出血点，浅表淋巴结未触及，眼睑无浮肿，巩膜无黄染。颈软，双肺呼吸音粗糙，未闻及干湿性罗音，心律不齐，第一心音强弱不等，各瓣膜区未闻及病理性杂音，腹平软，肝脾肋缘下未及，腹部无压痛及反跳痛，双肾区无叩击痛，双下肢未见凹陷性水肿；左侧前臂内瘘杂音响亮。\n" +
                "诊疗经过：\n" +
                "入院检查：血液分析:WBC 6.42G/L，NE% 63.4%，LY% 27.2%，RBC 4.14T/L，HGB 122g/L，PLT 217G/L。电解质：K+ 4.7mmol/L，Na+ 141.7mmol/L，Cl- 95.6mmol/L，Ca2+ 2.4mmol/L。肾功能：Urea 17.3mmol/L，Cr 1065.6umol/L，HCO3^- 23.0mmol/L。肝功能：ALT 5 IU/L，AST 7 IU/L，γ-GT 16 IU/L，ALP 185 IU/L，TP 84.0g/L，Alb 51.3g/L，胆红素正常。心肌酶：CK 50 IU/L，CK-MB 8 IU/L，LDH 193 IU/L，提示正常，排除心肌病变。心电图提示：心房纤颤，左室高电压，T波高耸（结合临床）。心脏彩超提示：符合高血压心脏声像图改变。完善大便常规及潜血均阴性。大便涂片未找到真菌，涂片：球菌10%，杆菌90%。Holter检查提示：窦性心律，偶发房性早搏，监测中可见一过性ST改变，心率变异性降低。入院给予血液净化、降压、纠正贫血、改善心肌供血、对症治疗，现病情好转，拟今日出院。\n" +
                "出院诊断：\n" +
                "出院情况：\n" +
                "患者未再出现心悸不适，未诉腹痛不适，未诉其它特殊不适，精神、食欲、睡眠一般。监测血压相对稳定。体格检查：慢性肾病面容，贫血貌，眼睑无浮肿，颈软，双肺呼吸音粗糙，未闻及干湿性罗音，心律齐，各瓣膜区未闻及病理性杂音，腹平软，肝脾肋缘下未及，腹部无压痛及反跳痛，双肾区无叩击痛，双下肢未见凹陷性水肿；左侧前臂内瘘杂音响亮。";
        termList = StandardTokenizer.segment(str);
        System.out.println(termList);
    }

    private static void testNLPTokenizer() {
        String str = "入院诊断：患者于三年前腹痛\n" +
                "入院情况：\n" +
                "患者因“诊断尿毒症4年余，反复心悸1天”入院，体检：T 36.3℃ P 94次/分 R 19次/分 BP 132/95mmHg；慢性肾病面容，贫血貌，全身皮肤黏膜无黄染及出血点，浅表淋巴结未触及，眼睑无浮肿，巩膜无黄染。颈软，双肺呼吸音粗糙，未闻及干湿性罗音，心律不齐，第一心音强弱不等，各瓣膜区未闻及病理性杂音，腹平软，肝脾肋缘下未及，腹部无压痛及反跳痛，双肾区无叩击痛，双下肢未见凹陷性水肿；左侧前臂内瘘杂音响亮。\n" +
                "诊疗经过：\n" +
                "入院检查：血液分析:WBC 6.42G/L，NE% 63.4%，LY% 27.2%，RBC 4.14T/L，HGB 122g/L，PLT 217G/L。电解质：K+ 4.7mmol/L，Na+ 141.7mmol/L，Cl- 95.6mmol/L，Ca2+ 2.4mmol/L。肾功能：Urea 17.3mmol/L，Cr 1065.6umol/L，HCO3^- 23.0mmol/L。肝功能：ALT 5 IU/L，AST 7 IU/L，γ-GT 16 IU/L，ALP 185 IU/L，TP 84.0g/L，Alb 51.3g/L，胆红素正常。心肌酶：CK 50 IU/L，CK-MB 8 IU/L，LDH 193 IU/L，提示正常，排除心肌病变。心电图提示：心房纤颤，左室高电压，T波高耸（结合临床）。心脏彩超提示：符合高血压心脏声像图改变。完善大便常规及潜血均阴性。大便涂片未找到真菌，涂片：球菌10%，杆菌90%。Holter检查提示：窦性心律，偶发房性早搏，监测中可见一过性ST改变，心率变异性降低。入院给予血液净化、降压、纠正贫血、改善心肌供血、对症治疗，现病情好转，拟今日出院。\n" +
                "出院诊断：\n" +
                "出院情况：\n" +
                "患者未再出现心悸不适，未诉腹痛不适，未诉其它特殊不适，精神、食欲、睡眠一般。监测血压相对稳定。体格检查：慢性肾病面容，贫血貌，眼睑无浮肿，颈软，双肺呼吸音粗糙，未闻及干湿性罗音，心律齐，各瓣膜区未闻及病理性杂音，腹平软，肝脾肋缘下未及，腹部无压痛及反跳痛，双肾区无叩击痛，双下肢未见凹陷性水肿；左侧前臂内瘘杂音响亮。";

        // NLP分词
        System.out.println("NLP分词");
        System.out.println(System.getProperty("user.dir"));
        System.out.println(NLPTokenizer.segment(str));
        System.out.println(NLPTokenizer.analyze(str).translateCompoundWordLabels());
    }


    public static void main(String[] args) {
        // 系统的classpaht路径
//        System.out.println(System.getProperty("java.class.path"));
        System.setProperty("HANLP_ROOT", "/Users/yuxuecheng/Learn/Source/github/java_git/");
        HanLP.Config.enableDebug();
        testPerceptronLexicalAnalyzer();
//        testNLPTokenizer();
//        testStandardTokenizer();
    }
}
