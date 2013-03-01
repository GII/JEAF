Some project dependencies cannot be satisfied from repositories. Using the jar files contained in project's lib/ folder, we must install them in our local repository when choosing the Maven option of development:

Project evolutionary_algorithms dependencies
--------------------------------------------

mvn install:install-file -Dfile=mpj.jar -DgroupId=org.hpjava -DartifactId=mpiJava -Dversion=1.2.5 -Dpackaging=jar
mvn install:install-file -Dfile=CMA_Jul2011.jar -DgroupId=fr.lri -DartifactId=cmaes -Dversion=jul-2011 -Dpackaging=jar
mvn install:install-file -Dfile=mtj-0.9.11.jar -DgroupId=no.uib.cipr -DartifactId=matrix-toolkits-java -Dversion=0.9.11 -Dpackaging=jar
