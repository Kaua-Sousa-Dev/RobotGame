<div align="center">

# 🤖 Jogo do Robô em JavaFX 🎮

**Um jogo 2D em grade feito em Java, com múltiplos modos de jogo, obstáculos aleatórios e uma interface gráfica moderna em JavaFX.**

</div>

---

## 🚀 Funcionalidades

* **🕹️ Interface Gráfica Completa:** O projeto foi totalmente refatorado de console para uma GUI com **JavaFX**, usando um `Stage` (janela) único para preservar o estado de tela cheia.
* **👾 Múltiplos Modos de Jogo:** Um menu principal permite escolher entre 4 modos de jogo distintos.
* **🎲 Obstáculos Aleatórios:** O Modo 4 gera Bombas (💣) e Montanhas (⛰️) em posições aleatórias a cada nova partida, garantindo que nenhum jogo seja igual ao outro.
* **🧠 IA Simples:** O "Robô Inteligente" (🎮) consegue detetar um movimento inválido (como bater numa parede) e tentar um movimento alternativo automaticamente.
* **🧱 Arquitetura MVC:** A lógica de jogo (Model/Engine) foi separada da interface (View/`GameScreen`), facilitando a manutenção.

---

## 🎮 Modos de Jogo Disponíveis

1.  **Modo 1: Jogador vs. Mapa**
    * Controle um único robô (🤖) através de comandos de texto (ex: "up" ou "1").
    * Baseado no `Main1.java` original.

2.  **Modo 2: Competição Aleatória**
    * Assista a uma corrida entre o Robô (🤖) e o Video Game (🎮), ambos a mover-se aleatoriamente.
    * Baseado no `Main2.java` original.

3.  **Modo 3: Normal vs. Inteligente**
    * Uma comparação de eficiência entre um Robô de movimento aleatório simples e o Robô Inteligente (com retentativa).
    * Baseado no `Main3.java` original.

4.  **Modo 4: Jogo Completo (Caos!)**
    * Os dois robôs (🤖 vs 🎮) competem num campo minado com bombas e montanhas aleatórias.
    * Baseado no `Main4.java` original.

---

## 🛠️ Como Executar o Projeto

Este projeto **requer o SDK do JavaFX** para ser executado.

### 1. Pré-requisitos

* **Java JDK 21** (ou superior)
* **SDK do JavaFX 21** (ou superior)
* **IntelliJ IDEA** (ou outra IDE compatível)

### 2. Configuração no IntelliJ IDEA

1.  **Clone o Repositório:**
    ```bash
    git clone https://github.com/Kaua-Sousa-Dev/RobotGame.git
    ```

2.  **Baixe o SDK do JavaFX:**
    * Faça o download no [site oficial da Gluon](https://gluonhq.com/products/javafx/).
    * Descompacte-o num local permanente (ex: `C:\Java\javafx-sdk-21`).

3.  **Adicione a Biblioteca:**
    * `File` > `Project Structure...` > `Libraries`.
    * Clique no `+` (Java) e aponte para a pasta `lib` do SDK do JavaFX (ex: `C:\Java\javafx-sdk-21\lib`).

4.  **Configure a Execução (Run Configuration):**
    * `Run` > `Edit Configurations...`.
    * Crie uma nova `Application` (+).
    * **Main class:** `principal.GameLauncher`
    * **VM options:** Clique em `Modify options` > `Add VM options`. No campo, cole o comando abaixo (ajuste o caminho para o seu SDK):

    ```bash
    --module-path "JavaFx\javafx-sdk-21\lib" --add-modules javafx.controls
    ```
    *(Este caminho é um exemplo baseado nos logs do projeto; substitua pelo seu caminho real.)*

5.  **Execute!**
    * Clique no botão Play (▶) para executar a configuração `GameLauncher`.
