# Prometheus
O Prometheus é uma ferramenta de monitoramento e alerta de aplicações, armazenando as métricas coletadas e possibilitando sua representação gráfica em séries temporais (identificadas pelo nome da métrica e por pares chave-valor opcionais chamados de *labels*). Os três componentes mais importantes da arquitetura dessa tecnologia são:
1. O servidor Prometheus, que coleta e armazena métricas (usando uma camada de persistência). Cada nó do servidor é autônomo e não depende de armazenamento distribuído;
2. A interface de usuário web, que permite o acesso e  a visualização dos dados armazenados. É interessante usar a ferramenta Grafana para acessar o servidor Prometheus usando PromQL (*Prometheus Query Language*);
3. O Alertmanager, que envia alertas de aplicações de clientes (especialmente do servidor Prometheus).

Assim, por meio de um arquivo de configuração `.yml`, a partir da utilização da ferramenta `Spring Boot Actuator`, que fornece uma forma de gerenciamento de dependências e de auto-configuração para o `Micrometer` - fachada de métricas da aplicação que suporta diversos sistemas de monitoramento (entre eles o Prometheus) -, é possível conectar a aplicação desenvolvida ao servidor do Prometheus para que se possa monitorar o funcionamento do projeto.

## Conceitos importantes
O Prometheus possui quatro tipos fundamentais de métricas:
- *Counter*: métrica cumulativa cujo valor só pode aumentar ou ser resetado para zero;
- *Gauge*: métrica que representa um valor número únca que pode crescer ou decrescer arbitrariamente;
- *Histogram*: amostra observações e as conta em pacotes configuráveis (chamados de *buckets*), fornecendo também a soma de todos os valores observados;
- *Summary*: similar ao histogram, amostra observações, fornecendo, além da contagem total de observações e a soma dos valores observados, quantis configuráveis ao longo de uma janela de tempo variável.

Além disso, o Prometheus agrupa o monitoramento de uma aplicação por *instances* e *jobs*. Nesse sentido, um *endpoint* que pode ser monitorado é chamado de *instance* (geralmente corresponde a um processo único). Uma coleção de *instances* com o mesmo propósito é chamada de *job*. 

## Adicionando o Spring Boot Actuator e o Micrometer à aplicação
Para adicionar o `Spring Boot Actuator` (que permitirá a exposição das métricas ao servidor do Prometheus) à aplicação, basta incluir sua [dependência](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator/2.3.0.RELEASE) ao arquivo `pom.xml`, como mostrado abaixo.
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
    <version>2.3.0.RELEASE</version>
</dependency>
```
Semelhantemente, deve-se acrescentar a [dependência](https://mvnrepository.com/artifact/io.micrometer/micrometer-registry-prometheus/1.5.1) relacionada ao `Micrometer` ao arquivo `pom.xml`, de modo que o `Spring Boot` é capaz de configurar automaticamente um registro capaz de coletar e exportar dados de métricas em um formato que o servidor do Prometheus é capaz de entender - conforme se mostra em seguida.
```
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <version>1.5.1</version>
</dependency>
```
Depois de configurar essas dependências, para acessar os *endpoints* expostos pelo `Spring Boot Actuator` via HTTP, é necessário incluir a propriedade `management.endpoints.web.exposure.include=*` no arquivo `application.properties` do projeto (localizado na pasta `src/main/resources`). Dessa forma, já é possível observar as métricas expostas pelo automaticamente pelo `Spring Boot Actuator` no *endpoint* `http://localhost:8080/actuator/prometheus` (considerando que a aplicação está rodando na porta 8080).

## Conectando o Spring Boot Actuator ao Prometheus
Inicialmente, adiciona-se um arquivo de configuração `prometheus.yml` à pasta `src/main/resources` do projeto (pode-se dar um outro nome a este arquivo ou colocá-lo em alguma outra pasta do sistema), como exposto a seguir.
```
# my global config
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).
  
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'
    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.
    static_configs:
    - targets: ['127.0.0.1:9090']

  - job_name: 'spring-actuator'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
    - targets: ['<HOST_IP>:8080']
```
Este é apenas um exemplo da configuração mais básica deste arquivo para permitir a comunicação entre o servidor do Prometheus e a aplicação (informações mais detalhadas sobre o o arquivo de configuração podem ser encontradas na [documentação oficial](https://prometheus.io/docs/prometheus/latest/configuration/configuration/)).
Deve-se notar, principalmente, o *job* `spring-actuator` no interior da seção `scrape_configs`. O atributo `metrics_path` indica o caminho ao *endpoint* `/prometheus` do `Spring Boot Actuator`. A seção `targets` contém o *host* e a porta da aplicação. É importante destacar que, como se pretende rodar o servidor do Prometheus em um container Docker, é preciso trocar o campo `<HOST_IP>` com o endereço IP da máquina em que a aplicação está sendo executada (não sendo possível utilizar `localhost`, nesse caso). 

Logo, para rodar o servidor Prometheus em um container Docker, basta utilizar o seguinte comando:
```

docker run -d --name=prometheus -p 9090:9090 -v <PATH_TO_prometheus.yml_FILE>:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml
```
Vale ressaltar que é preciso substituir `<PATH_TO_prometheus.yml_FILE>` pelo caminho do arquivo de configuração.
Finalmente, depois de rodar o container, é possível visualizar as métricas expostas ao Prometheus pela aplicação no endereço `http://localhost:9090` (em que se pode observar graficamente o resultado de uma determinada *query*, feita com *PromQL*).
 
## Integrando o Grafana
 Para rodar o Grafana em um container Docker, basta utilizar o comando: 
```
docker run -d --name=grafana -p 3000:3000 grafana/grafana
```
Então, é possível fazer login no Grafana, partir do endereço `http://localhost:3000` usando o usuário `admin` e a senha `admin` (sendo recomendado modificar a senha logo após o login).
Por fim, deve-se importar o Prometheus como fonte de dados para o Grafana (um guia para esta etapa pode ser encontrado [aqui](https://stackabuse.com/monitoring-spring-boot-apps-with-micrometer-prometheus-and-grafana/)). Adicionalmente, pode-se importar o [`JVM dashboard`](https://grafana.com/grafana/dashboards/4701) (*dashboard* pronto muito utilizado para aplicações Spring Boot) para facilitar a visualização dos dados.

## `Links úteis`
- Documentação oficial do Prometheus: https://prometheus.io/docs/introduction/overview/
- Documentação do Spring Boot Actuator: https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-monitoring
- Documentação do Spring Metrics: https://docs.spring.io/spring-metrics/docs/current/public/prometheus (documentação do Spring Metrics)
- Exemplo de conexão do Spring Boot Actuator com Prometheus/Grafana: https://www.callicoder.com/spring-boot-actuator-metrics-monitoring-dashboard-prometheus-grafana/
