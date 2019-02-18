Wiki

Запуск mvn spring-boot:run

Примеры запросов:

Создание продукта

curl --header "Content-Type: application/json" --request POST --data '{"name":"имя_продукта"}'  http://localhost:8080/product/add

Создание цены

curl --header "Content-T" --request POST --data '{"price":цена, "start_date":"2028-12-24", "end_date":"2029-12-24"}'  http://localhost:8080/price/add?productName=имя_продукта

для открытого интервала просто не прописывать дату

для удаления продукта

curl -X "DELETE" http://localhost:8080/product/delete?name=имя_продукта

Получение списка всех продуктов на определённую дату

curl http://localhost:8080/product/getAllPrices   (текущая дата)

с конкретной датой

curl http://localhost:8080/product/getAllPrices?date=2028-12-24

В формате имя продукта, цена и дата




  
