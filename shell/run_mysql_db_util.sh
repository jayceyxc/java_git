#!/usr/bin/env bash

java -cp target/java_git-jar-with-dependencies.jar com.linus.tools.MySqlDBUtil -f home_page.xls -c hospital_id,inpatient_id,name -l 100 -t home_page