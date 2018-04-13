# Yal
## Késako ?
Le langage ​*YAL* est un langage de programmation typé rudimentaire incluant les structures de contrôle élémentaires, les tableaux et les fonctions.

Le compilateur *YAL* est développé en Java, les générateurs d'analyseurs ​JFlex et ​JavaCup​ sont utilisés. Il génère du code ​MIPS​.

## Auteurs
Antonin Calba, Pierre Génard

## Des tests
```bash
#!/bin/bash

for i in `seq 30`; do sep="$sep="; done

for yal in *.yal
do
  echo $sep
  echo $yal
  java -jar yal.jar $yal | grep -A5 "^COMPILATION OK"
  [ $? -eq 0 ] && java -jar Mars4_5.jar dec v1 ${yal//yal/mips}
done

exit 0
```

## Assembleur MIPS
- [Télécharger MARS](http://courses.missouristate.edu/KenVollmar/MARS/download.htm)
- [Utiliser MARS en ligne de commande](https://courses.missouristate.edu/KenVollmar/mars/Help/MarsHelpCommand.html)

## Documentation 
- [MIPS Assembly Instructions](https://www2.cs.duke.edu/courses/fall13/compsci250/MIPS-ASM.pdf)
- [MIPS Assembly Language](http://service.scs.carleton.ca/sivarama/org_book/org_book_web/slides/chap_1_versions/ch15_1.pdf)
- [MIPS Instruction Reference](http://www.mrc.uidaho.edu/mrc/people/jff/digital/MIPSir.html)
- [MIPS Summary](http://www.cs.tufts.edu/comp/140/lectures/Day_3/mips_summary.pdf)



