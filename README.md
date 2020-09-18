## Installatie & runnen
### Met docker-compose
Bij de dockerfiles werd een **docker-compose.yaml** file voorzien.
Dit kan gebruikt worden om het project op docker te runnen.
De docker-compose moet in dezelfde map staan als die van de services.
In elke service-map bevindt zich ook een Dockerfile, dit kan gebruikt worden om de service
te builden bij het oproepen van `docker-compose build`.
De services werden echter ook toegevoegd aan **docker.io** bij de user *vmnaesse*.
Door de build-regels weg te doen in de docker-compose en de images te vervangen door **vmnaesse/[servicenaam]**,
hoeven de services niet meer gebuild te worden en kan direct `docker-compose up` gebruikt worden.
### Op kubernetes
De kubernetes files kunnen op kubernetes geactiveerd worden met het commando `kubectl apply -f file1,file2,..`.
Bij de kubernetes files werden voor het gemak ook create-scripts toegevoegd. Hiermee kunnen services op kubernetes
geactiveerd worden met het commando `./create_[servicenaam].sh`. Als alle services tergelijkertijd opgestart dienen te worden kan ook het script `./create_all.sh` gebruikt worden.
Achteraf kan alles terug opgekuist worden met `kubectl delete service service1 service2 ...` en `kubectl delete deployment service1 service2 ...`.
Ook hier werd voor het gemak een script gemaakt dat alles opkuist: `./cleanup_all.sh`.
Bij het maken van de kubernetes pods kan het tot rond 5 minuten duren voor alle services stabiel zijn.
## Testen
Om de services te testen kan gebruikt gemaakt worden van de api-gateway.
Navigeer naar *http://[ip van gateway]:8080* of *http://localhost:8080* indien met ssh de poort 8080 **geforward** werd.
Met deze indexpagina kan genavigeerd worden naar elk van de services.
Alle services hebben ook een index pagina waar mogelijke operaties voor testen opgelijst zijn.
Om **chaosmonkey** te testen op kubernetes dient het commando: `./chaos.sh` uitgevoerd te worden. Alle services behalve de databases,
de apigateway en messaging kunnen neergehaald worden.
## Code
De code kan altijd nog geraadpleedgd worden op onze github: https://github.ugent.be/karevera/sysontwerp2019
