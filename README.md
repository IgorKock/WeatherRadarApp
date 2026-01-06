## 🇧🇷 Português

Uma aplicação JavaFX que exibe informações meteorológicas em tempo real e mostra um mapa interativo com a localização da cidade escolhida.

### 🚀 Funcionalidades
- Busca o clima atual via [OpenWeatherMap API](https://openweathermap.org/api)
- Exibe temperatura e condições climáticas
- Mostra um mapa interativo com marcador da cidade (usando OpenLayers)
- Interface simples e limpa em JavaFX

### 🛠️ Tecnologias
- Java 21
- JavaFX (controles, web)
- Gson (para parse de JSON)
- Maven (gerenciamento de dependências)

### ▶️ Como executar
1. Clone o repositório:
   ```bash
   git clone https://github.com/IgorKock/WeatherRadarApp.git
   cd WeatherRadarApp
   ```
2. Compile e rode com Maven:
	```bash
	mvn clean install
	mvn javafx:run
	```
3. Digite o nome da cidade na interface para ver o clima e o mapa.

⚙️  Configuração

É necessário obter uma chave da API do OpenWeatherMap e substituir no código (API_KEY).

O mapa utiliza OpenStreetMap via OpenLayers (não requer chave).

📜 Licença

Este projeto é open source sob a licença MIT.

## 🇪🇳 English

A JavaFX application that displays real-time weather information and shows an interactive map with the selected city’s location.

### 🚀 Features
- Fetches current weather via [OpenWeatherMap API](https://openweathermap.org/api)
- Displays temperature and weather conditions
- Shows an interactive map with a city marker (using OpenLayers)
- Simple and clean JavaFX interface

### 🛠️ Technologies
- Java 21
- JavaFX (controls, web)
- Gson (for JSON parsing)
- Maven (dependency management)

### ▶️ How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/IgorKock/WeatherRadarApp.git
   cd WeatherRadarApp
   ```
2. Compile and run with Maven:
	```bash
	mvn clean install
	mvn javafx:run
	```
3. Enter a city name in the interface to see the weather and map.

⚙️ Configuration

You need an API key from OpenWeatherMap. Replace the API_KEY constant in the code.

The map uses OpenStreetMap via OpenLayers (no API key required).

📜 License

This project is open source under the MIT license.