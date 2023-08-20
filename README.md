# Local-Net Project
This project is to provide helpful services in local network environments, such as at home or in a office. Recently, a service for Real-time Dynamic Pricing in marketing has been developed.

## Dynamic Pricing System
The Dynamic Pricing System consists of a [backend](localNetServer) and a [frontend](localNetClient/hsgt_admin). The backend is to handle business logics, such as fetching seller data from the third-party RESTful APIs with API keys and then to store it to the local database. The data will be analyzed by the program which consequently suggests product prices to update. Recently, [Metro](https://developer.metro-selleroffice.com/docs/) and [Kaufland](https://sellerapi.kaufland.com/) APIs were considered in this project.

The [backend](localNetServer) was developed based on Spring Boot, a Spring-based application of Web service. For database, MySQL was chosen. In order to maintain data persistence, MyBatis, which automates the mapping between SQL databases and objects in Java, was used. 

The [frontend](localNetClient/hsgt_admin) template I used was fetched by the [vue-element-admin](https://github.com/PanJiaChen/vue-element-admin) project, a production-ready front-end solution for admin interfaces. This project provides various helpful templates of webpages, which allows quick frontend development.

