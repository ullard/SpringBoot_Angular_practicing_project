A fájl megnyitása valamilyen markdown szintaxist értelmezni képes szerkesztőben ajánlott.

Ajánlott online szerkesztők:

https://stackedit.io/

https://dillinger.io

########################################################################################################################################################################################################################################################
############################################################################################################################

# A fejlesztői környezethez felépítése (OSX rendszeren)

## Homebrew (package manager)
> A Homebrew egy csomagkezelő rendszer, ami leegyszerűsíti új szoftverek installálását és a meglévők karbantartását Mac OS X operációs rendszeren. Egy ingyenes/nyílt forrású szoftver, ami megkönnyíti más ingyenes/nyílt forrású szoftverek telepítését.

> 1. Homebrew telepítése
> 2. Homebrew Cask telepítése (kiterjeszti a Homebrew alkalmazást és lehetővé teszi nagy bináris fájlok telepítését)

> **Megjegyzés:** A Homebrew alapértelmezetten az összes csomagot a MacOS összes verziójában a következő könyvtárba telepíti: ```/usr/local/Cellar/```

## Állandó környezeti változók létrehozása
	
> Mivel az OSX bash shell-t használ, így a környezeti változók hozzáadhatók a .bash_profile könyvtárhoz az aktuális felhasználó számára. A könyvtár az alábbi elérési útvonalon található.

```bash
~/.bash_profile
```

> **Megjegyzés:** Ha nem található, akkor létre kell hozni. (ha ``nano`` szerkesztővel nyitjuk meg, akkor ez automatikusan létrehozza a fájlt, ha az még nem létezik)

> 1. Az adott program letöltése
> 2. Az letöltött program mappájának elérési útvonalának kivezetése környezeti változóba az alábbi módon

```bash
# terminálba - ez megnyitja (ha nem létezik, akkor létrehozza) a .bash_profile könyvtárat, amibe
# a változókat kell létrehozni

$ nano ~/.bash_profile
```

```bash
# a .bash_profile könyvtárba kell létrehozni változónként egy ilyen sort, amelyben megadjuk 
# a változó nevét, majd az abszolút elérési útvonalát

export VARIABLE_NAME=/path

# majd a PATH-ra fel kell fűzni

export PATH=/.../.../...:$VARIABLE_NAME/bin:$VARIABLE_NAME2/bin
```
> 3. Ha nano szerkesztőt használunk, akkor a CTRL+X gombokkal lépjünk ki, majd Y gombbal mentsük el a fájlt
> 4. Töltsük újra a .bash_profile fájlt az alábbi módon terminálból

```bash
$ source ~/.bash_profile
```

> 5. Indítsuk újra a terminált és ellenőrizzük, hogy létrejött-e a környezeti változónk

```bash
$ echo $VARIABLE_NAME
```

## Java telepítése

### Manuálisan

> 1. **Java SE** letöltése és telepítése (Oracle oldaláról)
> 2. ```$JAVA_HOME``` környezeti változó beállítása

```bash
$ nano ~/.bash_profile

export JAVA_HOME=$(/usr/libexec/java_home)

$ source ~/.bash_profile

# ellenőrzés

$ echo $JAVA_HOME
```

### Homebrew használatával

```bash
$ brew update
$ brew cask install java
```

## Tomcat telepítése
> Az Apache Tomcat egy tisztán Java nyelven készült webszerver, amely implementálja a Sun-féle Java Servlet és a JavaServer Pages specifikációkat.

> **Megjegyzés:** A következőkben a manuális telepítési részt nem fogom részletezni, mert mindegyik program esetében megegyezik az "Állandó környezeti változók létrehozása" részben található lépésekkel.

> 1. Telepítés **Homebrew** segítségével

```bash
$ brew update
$ brew install tomcat
```

> 2. Környezeti változó létrehozása

## Groovy telepítése
> A Groovy egy objektumorientált programozási nyelv a Java platformhoz. Hasonló tulajdonságokkal rendelkezik, mint a Python, a Ruby, a Perl és a Smalltalk. A Groovy szintaxisa Java-szerű, a blokkok határait kapcsos zárójelek jelzik. A Java-kód általában szintaktikailag helyes Groovy-ban is.

> 1. Telepítés **Homebrew** segítségével

```bash
$ brew update
$ brew install groovy
```

> 2. Környezeti változó létrehozása

## Spring Boot CLI telepítése
> A Spring Boot CLI egy parancssori eszköz, amelyet akkor használunk, ha gyorsan szeretnénk fejleszteni egy Spring alkalmazást. Ez lehetővé teszi a Groovy parancsfájlok futtatását, ami azt jelenti, hogy van egy ismerős Java-szerű szintaxis anélkül, hogy annyi boilerplate kód lenne.

> 1. Telepítés **Homebrew** segítségével

```bash
$ brew update
$ brew tap pivotal/tap
$ brew install springboot
```

> 2. Környezeti változó létrehozása

## Csomagoló keretrendszer telepítése
> Három fő Java automatizálási eszköz van, amelyek uralják a JVM ökoszisztémát. (Ant, Maven, Gradle)
> **Megjegyzés:** A három rendszer közül csak egy rendszer megléte szüksége.

### Ant telepítés
> Az Apache Ant egy Java könyvtár, amelyet a Java alkalmazások build folyamatainak automatizálására használnak.
Az Ant fő előnye a rugalmasság. Nem ír elő kódolási konvenciókat vagy projektstruktúrákat. Következésképpen ez azt jelenti, hogy az Ant megköveteli a fejlesztőktől, hogy az összes parancsot maguk írják, ami néha hatalmas XML build fájlokhoz vezet, amelyeket nehéz fenntartani. Mivel nincsenek konvenciók, csupán az Ant ismerete nem azt jelenti, hogy gyorsan megértjük az összes Ant összeállítási fájlt. Valószínűleg időbe telik, míg hozzászoknak egy ismeretlen Ant fájlhoz, ami hátrányt jelent a többi újabb eszközhöz képest.

> 1. Telepítés **Homebrew** segítségével

```bash
$ brew update
$ brew install ant
```

> 2. Környezeti változó létrehozása

### Maven telepítése
> Az Apache Maven egy szoftver, amelyet szoftverprojektek menedzselésére és a build folyamat automatizálására lehet használni. Funkcionalitásában hasonlít az Apache Ant eszközhöz.
Míg az Ant rugalmasságot igényel, és mindent a semmiből kell megírni, a Maven a konvenciókra támaszkodik, és előre meghatározott parancsokat (célokat) nyújt. A Maven másik pozitív aspektusa, hogy beépített támogatást nyújt a függőségek kezelésére.
A Maven nagyon népszerűvé vált, mivel az buildelési fájlokat szabványosították, és az Ant fájlhoz képest lényegesen kevesebb időbe telt a build fájlok fenntartása. Ugyanakkor, bár a Maven konfigurációs fájljai szabványosabbak, mint az Ant fájlok, azok továbbra is nagyobbak és nehézkesek. Maven szigorú konvenciói azzal járnak, hogy sokkal kevésbé rugalmasak, mint Ant. A cél testreszabása nagyon nehéz, ezért az Ant-hez képest sokkal nehezebb az egyedi build szkriptek írása.
Noha a Maven komoly fejlesztéseket tett az alkalmazás készítési folyamatainak könnyebbé és szabványosabbá tétele érdekében, ennek ellenére még mindig ára van, mivel sokkal kevésbé rugalmas, mint az Ant. Ez a Gradle létrejöttéhez vezetett, amely mindkét világ legjobban ötvözi - Ant rugalmassága és Maven tulajdonságai.

> 1. Telepítés **Homebrew** segítségével

```bash
$ brew update
$ brew install maven
```

> 2. Környezeti változó létrehozása

### Gradle telepítése
> A Gradle egy függőség menedzselő és build automatizáló eszköz, amelyet az Ant és Maven koncepcióira építettek. Nem használ XML fájlokat, mint az Ant és a Maven. A fejlesztők egyre inkább érdeklődtek a domain-specifikus nyelvek iránt és az azokkal való együttműködés iránt, ami lehetővé tenné számukra, hogy egy adott domain problémáit az adott tartományra szabott nyelv használatával megoldhassák. 
Ezt a Gradle fogadta el, amely a Groovy alapú DSL-t használja. Ez kisebb konfigurációs fájlokat eredményezett, kevesebb zavarral, mivel a nyelvet kifejezetten az adott tartományi problémák megoldására fejlesztették ki.
A Gradle lényegében szándékosan nagyon kevés funkcionalitást biztosít. A bővítmények hozzáadják az összes hasznos funkciót. 

> 1. Telepítés **Homebrew** segítségével

```bash
$ brew update
$ brew install gradle
```

> 2. Környezeti változó létrehozása

# Angular + Spring Boot projekt létrehozása
> A Spring Boot remekül alkalmazható, mint back end szerver egy Angular alkalmazás számára. A front end megírásához manapság olyan eszközöket használnak, mint a TypeScript, node.jsm npm és az Angular CLI.
A cél, hogy egyetlen olyan alkalmazás legyen, amely rendelkezik mind a Spring Boot, és mind az Angular funkcióival.

## Spring Boot alkalmazás létrehozása
> Egy Spring Boot alkalmazás létrehozásához használhatjuk az **IDE** funkcióit vagy a **Spring Boot CLI**-t.

```bash
# Spring Boot CLI használatával
$ curl start.spring.io/starter.tgz -d dependencies=web | tar -zxvf -
$ ./mvnw install
```

> **Megjegyzés:** Most vesszük ezt az alkalmazást, és hozzáadjuk az Angulart. Mielőtt bármit tehetnénk az Angularral, telepítenünk kell az npm-et.

## NPM telepítése lokálisan
> ```brew install node``` --> ```node --version``` & ```npm --version``` 

pom.xml

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
        <plugin>
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <!-- Use the latest released version:
                https://repo1.maven.org/maven2/com/github/eirslett/frontend-maven-plugin/ -->
                <version>LATEST_VERSION</version>
             </plugin>
            <configuration>
                <nodeVersion>NODE_VERSION</nodeVersion>
				<npmVersion>NPM_VERSION</npmVersion>
            </configuration>
            <executions>
                <execution>
                    <id>install-npm</id>
                    <goals>
                        <goal>install-node-and-npm</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

majd ezután

```bash
$ ./mvnw generate-resources
$ ls node*
```

## Angular CLI telepítése

> Egy Angular alkalmazás létrehozásánál valóban nagy segítség a CLI használata. Az ``npm`` használatával telepíthetjük, amit a plugin használatával kaptunk.

Hozzunk létre egy szkriptet az ``npm`` futtatásához.

```bash
$ cat > npm
#!/bin/sh
cd $(dirname $0)
PATH="$PWD/node/":$PATH
node "node/node_modules/npm/bin/npm-cli.js" "$@"
$ chmod +x npm
```

majd futtassuk a CLI telepítéséhez:

```bash
$ ./npm install @angular/cli
```

majd hozzunk létre egy hasonló csomagolót a CLI számára:

```bash
$ cat > ng
#!/bin/sh
cd $(dirname $0)
PATH="$PWD/node/":"$PWD":$PATH
node_modules/@angular/cli/bin/ng "$@"
$ chmod +x ng
$ ./ng --version
```

## Angular alkalmazás létrehozása

Az Angular CLI segítségével létrehozhatunk egy új alkalmazást. 

```bash
$ ./ng new client
```

majd áthelyezzük a projekt gyökerébe:

```bash
$ cat client/.gitignore >> .gitignore
$ rm -rf client/node* client/src/favicon.ico client/.gitignore client/.git
$ sed -i -e 's/node_/anode/' .gitignore
$ cp -rf client/* .
$ cp client/.??* .
$ rm -rf client
$ sed -i -e 's,dist/client,target/classes/static,' angular.json
```

Eldobásra kerültek azok a node modulok, amelyeket a CLI telepített, mert azt akarjuk, hogy a front end plugin csinálja helyette egy automatizált folyamatban. Az ``angular.json`` fájlon is módosítottunk, hogy az Angular build kimenetét átirányítsuk egy olyan helyre, amit belecsomagolunk a JAR fájlunkba. 

## Building
Hozzáadunk egy végrehajtást (execution) az alkalmazásban használt modulok telepítéséhez:

```xml
<execution>
	<id>npm-install</id>
	<goals>
		<goal>npm</goal>
	</goals>
</execution>
```

Futtassuk újra az ```./mvnw generate-resources``` parancsot a modulok telepítéséhez, majd ellenőrizzük az eredményt:

```bash
$ ./ng version
```

Adjuk hozzá a következő végrahajtást (execution) még, hogy a Maven build közben leforduljon a kliens alkalmazás:

```xml
<execution>
	<id>npm-build</id>
	<goals>
		<goal>npm</goal>
	</goals>
	<configuration>
		<arguments>run-script build</arguments>
	</configuration>
</execution>
```

> **Megjegyzés:** Az alkalmazás ezután a ```$ mvn spring-boot:run``` paranccsal futtatható.

## Bootstrap hozzáadása
> Hozzáadhatunk alapvető Twitter Bootstrap funkciókat, hogy javítsuk az alkalmazásunk megjelenésén.

```bash
$ ./npm install bootstrap@3 jquery --save
```

majd frissítsük a ``style.css`` fájlt:

```typescript
@import "~bootstrap/dist/css/bootstrap.css";
```

# SSL kapcsolathoz létrehozott tanúsítvány hozzáadása a Java keystorehoz
OSX alatt a ``$JAVA_HOME`` megtalálásához futtassuk a következő parancsot:

```bash
$ /usr/libexec/java_home
```

A ``cacerts`` ezen belül a ```/lib/security/cacerts``` mappában találhatók:

```bash
$(/usr/libexec/java_home)/lib/security/cacerts
```

vagy itt, attól függően, hogy a JDK telepítve van vagy sem:

```bash
$(/usr/libexec/java_home)/jre/lib/security/cacerts
```

Hozzáadás példa:
```bash
$ sudo keytool -importcert -keystore /Library/Java/JavaVirtualMachines/jdk-11.0.2.jdk/Contents/Home/lib/security/cacerts -storepass changeit -file /Volumes/MyPassportU/Others/Folders/STS_Workspace_OSX/Diploma_project/_keys_/auth.crt -alias authcert
```

# Jasypt használata a programon belüli jelszavak kódolásához
> A Jasypt egy Java könyvtár, amely lehetővé teszi a fejlesztő számára, hogy minimális erőfeszítéssel és alapvető titkosítási képességekkel bővítse projektjeit, anélkül, hogy mély ismeretekkel rendelkezne a kriptográfia működéséről.

A Jasypt segítségével titkosíthatjuk a ```.properties``` fájlokban található jelszavakat, mint például egy levelező szerverhez vagy egy adatbázishoz való kapcsolódás során használt jelszót:

```bash
spring.mail.username = email.address@gmail.com
spring.mail.password = ENC(DpWUlGFTE4zM5Kkuh4Ax1EdBt6mu+1HP) #ebben az esetben a titkosított jelszó: "testpassword" és a titkosító kulcs pedig: "password"
```

Ehhez annyit kell tennünk, hogy a pom.xml fájlba beimportáljuk a következő dependenciát:

```xml
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

majd a titkosítani kívánt jelszót titkosítjuk és a kapott, már titkosított jelszót belehelyezzük egy ```ENC()```-be.

> **Megjegyzés:** Ha használjuk a titkosítást, akkor az alkalmazást ugyanazzal a jelszóval kell elindítanunk, amit a titkosításhoz használtunk, például: ```mvn -Djasypt.encryptor.password=password spring-boot:run``` vagy az IDE-ben a ``futtatási konfigurációk`` alatt meg kell adni argumentumként ```-Djasypt.encryptor.password=password```.



