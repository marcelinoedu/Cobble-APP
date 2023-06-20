## Setup de ambiente local:

## local environment, database-path :
``
config-server: localhost:8888 
Discovey: localhost:8761
Gateway: localhost:8222
Auth-server: localhost:8889 -> mysql://127.0.0.1:3306/db_user-manager || mysql://127.0.0.1:3306/db_admin-manager
User-facade: localhost:8085
Admin-facade: localhost:8095
UserManager: localhost:8080 -> mysql://127.0.0.1:3306/db_user-manager
AdminManager: localhost:8090 -> mysql://127.0.0.1:3306/db_user-manager || mysql://127.0.0.1:3306/db_admin-manager
FinanceManager: localhost:8081  -> mysql://127.0.0.1:3306/db_finance-manager
InvstmentManager: localhost:8110 -> mysql://127.0.0.1:3306/db_investment-manager
Zipkin: localhost:9411
``
