package athletia.service.impl;

import athletia.model.User;
import athletia.model.WorkoutPlanGenerated;
import athletia.service.WorkoutPlanAIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class WorkoutPlanAIServiceImpl implements WorkoutPlanAIService {

    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    public WorkoutPlanAIServiceImpl() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String apiKey = dotenv.get("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is missing from .env");
        }

        this.openAiService = new OpenAiService(apiKey);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public WorkoutPlanGenerated generateWorkoutPlan(User user) {
        int age = Period.between(user.birthDate(), LocalDate.now()).getYears();

        String exerciseCatalog = """
                Use somente os seguintes exercícios (nome + grupo muscular + id):
                - Supino Reto com Barra (Peito) => ID: supino_reto_com_barra_peito
                - Supino Inclinado com Halteres (Peito) => ID: supino_inclinado_com_halteres_peito
                - Flexão de Braço (Peito) => ID: flexao_de_braco_peito
                - Agachamento Livre (Pernas) => ID: agachamento_livre_pernas
                - Cadeira Extensora (Pernas) => ID: cadeira_extensora_pernas
                - Leg Press (Pernas) => ID: leg_press_pernas
                - Remada Curvada com Barra (Costas) => ID: remada_curvada_com_barra_costas
                - Puxada na Frente (Costas) => ID: puxada_na_frente_costas
                - Barra Fixa (Costas) => ID: barra_fixa_costas
                - Desenvolvimento com Halteres (Ombros) => ID: desenvolvimento_com_halteres_ombros
                - Elevação Lateral (Ombros) => ID: elevacao_lateral_ombros
                - Bíceps Rosca Direta (Bíceps) => ID: biceps_rosca_direta_biceps
                - Rosca Martelo (Bíceps) => ID: rosca_martelo_biceps
                - Tríceps Testa com Barra (Tríceps) => ID: triceps_testa_com_barra_triceps
                - Tríceps Pulley (Tríceps) => ID: triceps_pulley_triceps
                - Prancha (Core) => ID: prancha_core
                - Abdominal Crunch (Abdômen) => ID: abdominal_crunch_abdomen
                - Corrida na Esteira (Cardiorrespiratório) => ID: corrida_na_esteira_cardiorrespiratorio
                - Bicicleta Ergométrica (Cardiorrespiratório) => ID: bicicleta_ergometrica_cardiorrespiratorio
                - Escada Simuladora (Pernas) => ID: escada_simuladora_pernas

                Utilize apenas os nomes e IDs exatos acima. Para cada exercício, inclua o campo "exerciseId" com o ID correspondente. Não invente exercícios ou traduções. Escolha com base no objetivo e nível do usuário.
                """;

        String prompt = String.format("""
                Você é uma IA treinadora pessoal.
                Gere um plano de treino em JSON para o seguinte usuário:
                - Idade: %d
                - Gênero: %s
                - Altura: %.2f cm
                - Peso: %.2f kg
                - Nível: %s
                - Objetivo: %s

                %s

                Responda SOMENTE com um JSON válido no seguinte formato:
                {
                  "title": "string",
                  "description": "string",
                  "durationWeeks": 4,
                  "exercises": [
                    {
                      "exerciseId": "string",
                      "name": "string",
                      "muscleGroup": "string",
                      "sets": 3,
                      "reps": 12,
                      "restSeconds": 60,
                      "suggestedLoad": 30.0
                    }
                  ]
                }

                Não inclua explicações, comentários ou valores fictícios. Apenas retorne o JSON. Lembre-se de retornar os exercícios baseado (ao menos 8 exercícios) nas características da pessoa e do que ela precisa.
                """,
                age, user.gender(), user.height(), user.weight(), user.level(), user.goal(), exerciseCatalog);

        ChatMessage system = new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Você é uma IA de treino que responde somente com JSON válido e compatível com o modelo esperado.");
        ChatMessage userMsg = new ChatMessage(ChatMessageRole.USER.value(), prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(system, userMsg))
                .temperature(0.4)
                .maxTokens(1000)
                .build();

        String json = openAiService.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();

        try {
            System.out.println("🔍 IA RESPONSE:");
            System.out.println(json);
            return objectMapper.readValue(json, WorkoutPlanGenerated.class);
        } catch (Exception e) {
            System.err.println("❌ Erro ao fazer parse da IA:");
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar plano com IA", e);
        }

    }
}
