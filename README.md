# Auction web app

## How To Use :wrench:

To clone and run this application, you will need [Git](https://git-scm.com), [Java](https://www.oracle.com/java/technologies/javase-downloads.html),
[Maven](https://maven.apache.org/download.cgi) and [Node.js](https://nodejs.org/en/download/).

```bash
# Clone this repository
$ git clone https://github.com/kkadusic/auction-app

# Go into the root directory
$ cd auction-app
```

- ##### Backend application
```bash
# Go into the backend directory
$ cd backend

# Run the server
$ mvn spring-boot:run
```

- ##### Frontend application
```bash
# Go into the fronted directory
$ cd frontend

# Install dependencies
$ npm install

# Run the client
$ npm start
```

Frontend application: [`https://auction-abh.herokuapp.com`](https://auction-abh.herokuapp.com/) <br>
Backend application: [`https://auction-abh-server.herokuapp.com`](https://auction-abh-server.herokuapp.com/) <br>
Open [`https://auction-abh-server.herokuapp.com/swagger-ui.html`](https://auction-abh-server.herokuapp.com/swagger-ui.html) to launch Swagger UI in a browser.
