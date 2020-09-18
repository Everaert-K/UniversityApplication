# sysontwerp2019

## Stappenplan voor het niet fucken van de github
- Maak een nieuwe map aan (vb Systeemontwerp)
- type "git init" in deze map
- type "git pull https://github.ugent.be/karevera/sysontwerp2019.git" in deze map
- Bestanden zouden nu in de map moeten staan
- Start SpringToolSuite4 op
- Zet de workdirectory (hetgene dat eerst gevraagd wordt) gelijk aan de nieuwe map (hier Systeemontwerp)
- Springtoolsuite zou nu moeten openen
- Maak een nieuw Spring Starter project aan --> naamgeving systeemontwerp-xxxxxx-service, packagenaam com.systeemontwerp.xxxxservice
- Save project en sluit SpringToolSuite4
- Type "git status" --> de map met je nieuwe project zo hier in het rood moeten staan
- Type "git config --global user.name "jegithubnaam" (bij mij was dit vmnaesse)
- Type "git config --global user.email "ugentemail" (bij mij was dit Vince.Naessens@UGent.be)
- Type "git add naamvandemapdieroodwas"
- Type "git commit -m "berichtjewatveranderdis"
- Type "git push" <-- zal eerst niet werken maar test toch maar
- Type "git remote add Systeemontwerp https://github.ugent.be/karevera/sysontwerp2019"
- Type "git push --set-upstream Systeemontwerp master" <-- je bestanden zouden moeten pushen nu
- Joepie, staat op github
- Elke keer je wil aanpassen -> git fetch (om te kijken of er nieuwe commits zijn van anderen), git pull (om deze eventueel binnen te halen), git status (om te checken of je nog gelijk bent met de master)
- Best ook niet in andere services werken zonder die persoon iets te melden om merge errors te vermijden
- Na elke aanpassing uit te voeren: git status, git add bestandendieroodzijnindestatus, git push
