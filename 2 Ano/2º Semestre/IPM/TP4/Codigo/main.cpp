#include <WiFi.h>
#include <WebServer.h>

const int motor1 = 18;  // LED representando o motor esquerdo
const int motor2 = 19;  // LED representando o motor direito

// Variáveis para controlar o estado dos motores
int motor1State = LOW;
int motor2State = LOW;

// Configurações do Wi-Fi do Access Point
const char* apSSID = "ESP32-AP";  // Nome da rede Wi-Fi criada pelo ESP32 Vodafone-8E9690 ESP32-AP
const char* apPassword = "123456789";  // Senha da rede Wi-Fi criada pelo ESP32  tB4T3cb2SJxB5fUT 123456789

// Cria uma instância do servidor web na porta 80
WebServer server(80);

// Função para a página principal
void handleRoot() {
  String html = "<html><body>";
  html += "<h1>Controle dos Motores</h1>";
  html += "<a href='/led/on'><button>Acender o LED</button></a><br><br>";
  html += "<a href='/led/off'><button>Apagar o LED</button></a><br><br>";
  html += "</body></html>";
  
  // Envia a resposta para o cliente (navegador)
  server.send(200, "text/html", html);
}

// Função para acender o LED
void turnLedOn() {
  motor1State = HIGH;
  motor2State = HIGH;
  digitalWrite(motor1, motor1State);
  digitalWrite(motor2, motor2State);

  // Responde com uma mensagem de sucesso
  server.send(200, "text/html", "<html><body><h1>LED Aceso!</h1><a href='/'>Voltar</a></body></html>");
}

// Função para apagar o LED
void turnLedOff() {
  motor1State = LOW;
  motor2State = LOW;
  digitalWrite(motor1, motor1State);
  digitalWrite(motor2, motor2State);

  // Responde com uma mensagem de sucesso
  server.send(200, "text/html", "<html><body><h1>LED Apagado!</h1><a href='/'>Voltar</a></body></html>");
}

void setup() {
  // Inicializa a comunicação serial
  Serial.begin(115200);
  
  Serial.println("Inicio");
  // Configura os pinos dos motores (LEDs) como saída
  pinMode(motor1, OUTPUT);
  pinMode(motor2, OUTPUT);

  // Configura o ESP32 como Access Point
  WiFi.softAP(apSSID, apPassword);
  Serial.println("ESP32 funcionando como Access Point");

  // Exibe o IP do ESP32 (será o endereço da rede Wi-Fi)
  Serial.print("Endereço IP: ");
  Serial.println(WiFi.softAPIP());

  // Definir as rotas para o servidor
  server.on("/", HTTP_GET, handleRoot);  // Página principal
  server.on("/led/on", HTTP_GET, turnLedOn);  // Acender o LED
  server.on("/led/off", HTTP_GET, turnLedOff);  // Apagar o LED

  // Inicia o servidor
  server.begin();
}

void loop() {
  // Escuta as requisições HTTP
  server.handleClient();
}
