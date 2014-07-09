projekt napravljen u sklopu Oblikovanja Programske Potpore.
Instant Messenger s GUI-jem, ima funkcionalnosti poput registriranja accounta, loginnanja, databaza koja čuva registrirane accountove, friend liste, grupni razgovori, adminska aplikacija, čuvanje poruka dobivenih dok je klijent offline tako da mu se mogu prikazati kada se loginna itd..
Runnable jar fileovi služe za jednostavno isprobavanje programa.
Redoslijed pokretanja .jar fileova
1. Database.jar - za inicijaliziranje baze (na startu se ubace 4 accountova s oblikom username:password - user1:pass1, user2:pass2, user3:pass3, admin:pass), potrebno samo jedanput
2. Server.jar - nema GUI, tako da će se vrtiti stalno u pozadini dok ga se ne ugasi s
nečime poput Task Managera :)
3. Client.jar - moguće pokrenuti više puta za simuliranje više korisnika, loginnanje sa
admin:pass se dobiva pristup posebnoj adminskoj verziji aplikacije