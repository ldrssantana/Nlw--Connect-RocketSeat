📌 Projeto Db_events

📂 Clonar o repositório
# Clone o repositório
git clone https://github.com/SeuUsuario/NomeDoRepositorio.git

⚙️ Configurar o ambiente

Verifique se o arquivo docker-compose.yml está corretamente configurado.

Inicie os contêineres:
docker-compose up -d

Arquivo docker-compose.yml
 services:
  mysql:
    image: mysql:8.4
    restart: always
    container_name: mysql-nlw
    environment:
      - MYSQL_ROOT_PASSWORD=mysql
    ports: 
      - '3336:3306'
    networks: 
      - nlw-network
networks:
  nlw-network:
    driver: bridge

Configuração do Banco de Dados
**terminar
