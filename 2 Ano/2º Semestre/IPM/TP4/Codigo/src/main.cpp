#include <WiFi.h>
#include <WebServer.h>

// Definindo os pinos dos motores (ou LEDs) como exemplo
const int motor1 = 18;  // Motor 1 (esquerda)
const int motor2 = 19;  // Motor 2 (direita)

// Configurações do Wi-Fi do Access Point
const char* apSSID = "gouveia";  // Nome da rede Wi-Fi       ESP32-AP  Vodafone-8E9690
const char* apPassword = "gouveia22";  // Senha da rede Wi-Fi 123456789  tB4T3cb2SJxB5fUT

// Configuração de IP estático para uso de cliente
IPAddress local_IP(192, 168, 99, 130);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);
IPAddress primaryDNS(8, 8, 8, 8);
IPAddress secondaryDNS(8, 8, 4, 4);


// Cria uma instância do servidor na porta 80
WebServer server(80);

// Função para lidar com a página inicial
void handleRoot() {
  String html = "<!DOCTYPE html><html><body>";
  html += "<h1>Controle com Olhos, Voz e Clique</h1>";
  html += "<button onclick=\"fetch('/comando?command=frente')\">Frente</button>";
  html += "<button onclick=\"fetch('/comando?command=direita')\">Direita</button>";
  html += "<button onclick=\"fetch('/comando?command=esquerda')\">Esquerda</button>";
  html += "<button onclick=\"fetch('/comando?command=esquerda')\">Parar</button>";
  html += "</body></html>";
  server.send(200, "text/html", html); // Envia a página HTML
}

// Função para lidar com os comandos de controle
void handleCommand() {
  String command = server.arg("command");  // Obtém o parâmetro 'command' da requisição

  Serial.println("Comando recebido: " + command);  
  server.sendHeader("Access-Control-Allow-Origin", "*");
  server.sendHeader("Access-Control-Allow-Methods", "GET, POST");
  server.sendHeader("Access-Control-Allow-Headers", "Content-Type");

  if (command == "frente") {
    digitalWrite(motor1, HIGH);  // Acende o LED 1 (motor 1)
    digitalWrite(motor2, HIGH);   // Apaga o LED 2 (motor 2)
    server.send(200, "text/plain", "Comando: Frente");
  }
  else if (command == "esquerda") {
    digitalWrite(motor1, LOW);   // Apaga o LED 1 (motor 1)
    digitalWrite(motor2, HIGH);  // Acende o LED 2 (motor 2)
    server.send(200, "text/plain", "Comando: Esquerda");
  }
  else if (command == "direita") {
    digitalWrite(motor1, HIGH);   // Apaga o LED 1 (motor 1)
    digitalWrite(motor2, LOW);   // Apaga o LED 2 (motor 2)
    server.send(200, "text/plain", "Comando: Direita");
  }else if (command == "parar") {
    digitalWrite(motor1, LOW);   // Apaga o LED 1 (motor 1)
    digitalWrite(motor2, LOW);   // Apaga o LED 2 (motor 2)
    server.send(200, "text/plain", "Comando: Direita");
  }
  else {
    server.send(400, "text/plain", "Comando não reconhecido");
  }
}

void setup() {
  // Inicializa a comunicação serial
  Serial.begin(115200);
  
  // Configura os pinos dos motores (ou LEDs) como saída
  pinMode(motor1, OUTPUT);
  pinMode(motor2, OUTPUT);

  // Configura IP estático em caso de cliente
  if (!WiFi.config(local_IP, gateway, subnet, primaryDNS, secondaryDNS)) {
    Serial.println("Falha na configuração do IP estático");
  }

  // Conecta à rede Wi-Fi como cliente
  WiFi.begin(apSSID, apPassword);
  Serial.println("A conectar-se à rede Wi-Fi...");

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("Conectado!");
  Serial.print("Endereço IP atribuído: ");
  Serial.println(WiFi.localIP()); //usar como cliente
  //Serial.println(WiFi.softAP(apSSID, apPassword));

  // Configura as rotas do servidor
  server.on("/", handleRoot); // Página principal
  server.on("/comando", HTTP_GET, handleCommand);  // Comando GET para controle

  // Inicia o servidor
  server.begin();
  Serial.println("Servidor HTTP iniciado");
}

void loop() {
  // Lida com as requisições HTTP
  server.handleClient();
}
