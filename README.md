# WeatherViewer App - Previsão do Tempo

Trabalho prático da disciplina de **Programação III**, desenvolvido com base no Capítulo 7 do livro "Android for Programmers", adaptado para consumir uma API REST personalizada hospedada na AWS.

## 👨‍💻 Integrantes
* **Alunos:**
* Roberto Henrique Duarte
* João Victor Costa Arruda
* **Professor:** Eduardo Henrique Marques Ferreira
* **Curso:** Sistemas de Informação
* **Disciplina:** Programação III (2025/02)

## 🎯 Objetivo do Projeto

O objetivo deste aplicativo é consumir um Web Service RESTful de previsão do tempo e exibir os dados de forma amigável ao usuário. Diferente do exemplo original do livro (que usa OpenWeatherMap), este projeto foi adaptado para:
1.  Consumir uma API específica fornecida pelo professor (hospedada na AWS).
2.  Processar uma estrutura JSON simplificada (array `days`).
3.  Exibir ícones meteorológicos utilizando Emojis (texto) em vez de download de imagens.
4.  Realizar validações de entrada no lado do cliente.

## 🚀 Funcionalidades Implementadas

* **Busca de Cidade:** Permite digitar o nome da cidade (ex: `Passos, MG, BR`) para consultar a previsão.
* **Validação de Entrada:** Bloqueia buscas com menos de 3 caracteres para evitar requisições inválidas.
* **Conexão Assíncrona:** Utiliza `AsyncTask` para realizar chamadas de rede sem travar a interface do usuário (UI Thread).
* **Feedback Visual:** Exibe status "Buscando..." e limpa a lista anterior ao iniciar uma nova pesquisa.
* **Exibição de Dados:** Lista personalizada contendo:
    * Ícone do tempo (Emoji).
    * Data e Descrição do clima.
    * Temperaturas Mínima e Máxima (formatadas em °C).
    * Umidade relativa do ar.
* **Confirmação de Local:** Exibe o nome oficial da cidade retornado pela API para confirmar a busca.



## 📱 Screenshots

|            Tela Inicial / Busca             |            Resultado da Previsão             | 
|:-------------------------------------------:|:--------------------------------------------:|
| <img src="screenshots/img.png" width="200"> | <img src="screenshots/img_1.png" width="200"> | 



## 🛠️ Como Executar a Aplicação

### Pré-requisitos
* Android Studio instalado.
* Emulador Android ou dispositivo físico com Android 6.0 ou superior.
* Conexão ativa com a Internet.

### Passo a Passo
1.  Clone este repositório:
    ```bash
    git clone [https://github.com/rhbebeto/WeatherViewer-App](https://github.com/rhbebeto/WeatherViewer-App)
    ```
2.  Abra o projeto no **Android Studio**.
3.  Aguarde o sincronismo do Gradle.
4.  Execute a aplicação clicando no botão **Run** (▶️).
5.  No campo de texto, digite uma cidade seguindo o padrão `Cidade, Estado, Pais`.
    * Exemplo: `Passos, MG, BR`
    * Exemplo: `Monte Santo de Minas, MG, BR`


