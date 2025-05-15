# 🏋️‍♀️ AthletIA

**AthletIA** é um aplicativo de treino personalizado com IA embutida, desenvolvido com **Java + Spring Boot** no backend e **React Native** no frontend.

> MVP focado em gerar planos de treino inteligentes com base no perfil do usuário.

---

## 🚀 Tecnologias

| Camada        | Stack                   |
|---------------|-------------------------|
| Backend       | Java 21 + Spring Boot 3 |
| Banco de dados| MongoDB Atlas           |
| Frontend      | React Native (mobile)   |
| Autenticação  | JWT (stateless)         |
| Infraestrutura| Docker + Railway        |
| IA inicial    | Regras baseadas em perfil |

---

## 📦 Funcionalidades do MVP

- [x] Cadastro e login com JWT
- [x] Perfil do usuário (altura, peso, nível, etc)
- [x] Geração de plano de treino com IA baseada em perfil
- [x] Consulta de plano e exercícios
- [x] Registro da execução do treino

---

## 🧠 Estrutura de Domínio

User
└── WorkoutPlan
└── WorkoutExercise
└── Exercise (catálogo fixo)

---

## 🧪 Testes

- Testes unitários com JUnit 5
- Testes de controller com MockMvc
- Testcontainers para MongoDB

---

## 🔧 Como rodar o projeto

```bash
# backend
cd backend
./mvnw spring-boot:run

# frontend
cd frontend
npx react-native run-android

```
---
💡 Próximas features
- Ajuste automático de carga com base no desempenho
- Feedback por IA após o treino
- OAuth2 com Google
- Análise de progresso com gráficos
---

👩‍💻 Feito por
@vicprandi ❤️

---
