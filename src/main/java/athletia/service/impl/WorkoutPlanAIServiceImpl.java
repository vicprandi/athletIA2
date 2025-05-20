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
            - Crucifixo Reto com Halteres (Peito) => ID: crucifixo_reto_com_halteres_peito
            - Flexão de Braço (Peito) => ID: flexao_de_braco_peito
            - Pullover com Halter (Peito) => ID: pullover_com_halter_peito
            - Crucifixo na Máquina (Peito) => ID: crucifixo_na_maquina_peito
            - Voador Peitoral (Peito) => ID: voador_peitoral_maquina_peito
            - Agachamento Livre (Pernas) => ID: agachamento_livre_pernas
            - Cadeira Extensora (Pernas) => ID: cadeira_extensora_pernas
            - Leg Press (Pernas) => ID: leg_press_pernas
            - Passada com Halteres (Pernas) => ID: passada_com_halteres_pernas
            - Subida no Banco com Halteres (Pernas) => ID: subida_no_banco_com_halteres_pernas
            - Afundo Búlgaro (Pernas) => ID: afundo_bulgaro_pernas
            - Stiff com Barra (Posterior de Coxa) => ID: stiff_com_barra_posterior
            - Cadeira Flexora (Posterior de Coxa) => ID: cadeira_flexora_posterior
            - Elevação de Panturrilha em Pé (Panturrilha) => ID: panturrilha_em_pe_maquina_panturrilha
            - Elevação de Panturrilha Sentado (Panturrilha) => ID: panturrilha_sentado_maquina_panturrilha
            - Glúdeo na Polia (Glúdeos) => ID: gluteo_na_polia_gluteos
            - Kickback com Halter (Glúdeos) => ID: kickback_com_halter_gluteos
            - Remada Curvada com Barra (Costas) => ID: remada_curvada_com_barra_costas
            - Remada Unilateral com Halter (Costas) => ID: remada_unilateral_com_halter_costas
            - Remada Baixa na Polia (Costas) => ID: remada_baixa_na_polia_costas
            - Puxada Frontal na Polia (Costas) => ID: puxada_frontal_maquina_costas
            - Barra Fixa (Costas) => ID: barra_fixa_costas
            - Encolhimento com Halteres (Trapézio) => ID: encolhimento_com_halteres_trapezio
            - Encolhimento na Máquina (Trapézio) => ID: encolhimento_na_maquina_trapezio
            - Desenvolvimento com Halteres (Ombros) => ID: desenvolvimento_com_halteres_ombros
            - Desenvolvimento na Máquina (Ombros) => ID: desenvolvimento_maquina_ombros
            - Elevação Lateral (Ombros) => ID: elevacao_lateral_ombros
            - Elevação Frontal com Halteres (Ombros) => ID: elevacao_frontal_ombros
            - Crucifixo Inverso na Máquina (Ombros) => ID: crucifixo_inverso_maquina_ombros
            - Rosca Direta com Barra (Bíceps) => ID: biceps_rosca_direta_biceps
            - Rosca Martelo (Bíceps) => ID: rosca_martelo_biceps
            - Rosca Concentrada (Bíceps) => ID: rosca_concentrada_biceps
            - Rosca Scott na Máquina (Bíceps) => ID: rosca_scott_maquina_biceps
            - Rosca Inversa com Barra (Bíceps) => ID: rosca_inversa_barra_biceps
            - Tríceps Pulley (Tríceps) => ID: triceps_pulley_triceps
            - Tríceps Testa com Barra (Tríceps) => ID: triceps_testa_com_barra_triceps
            - Tríceps Coice com Halter (Tríceps) => ID: triceps_coice_com_halter_triceps
            - Tríceps Francês com Halter (Tríceps) => ID: triceps_frances_com_halter_triceps
            - Mergulho entre Bancos (Tríceps) => ID: mergulho_banco_triceps
            - Abdominal Crunch (Abdômen) => ID: abdominal_crunch_abdomen
            - Abdominal na Máquina (Abdômen) => ID: abdominal_na_maquina_abdomen
            - Twist Russo (Abdômen) => ID: twist_russo_abdomen
            - Prancha (Core) => ID: prancha_core
            - Prancha Lateral (Core) => ID: prancha_lateral_core
            - Corrida na Esteira (Cardiorrespiratório) => ID: corrida_na_esteira_cardiorrespiratorio
            - Bicicleta Ergométrica (Cardiorrespiratório) => ID: bicicleta_ergometrica_cardiorrespiratorio
            - Escada Simuladora (Cardiorrespiratório) => ID: escada_simuladora_cardiorrespiratorio
            - Polichinelo (Cardiorrespiratório) => ID: polichinelo_cardiorrespiratorio
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

                Leve em consideração idade, peso e nível de treinamento para adaptar a carga, volume e intensidade dos exercícios.

                %s

                Regras importantes:
                - Mínimo de 8 exercícios
                * Iniciante: escolha entre 2 ou 3 séries, e entre 10, 12 ou 15 repetições
                * Intermediário: escolha entre 3 ou 4 séries, e entre 8, 10 ou 12 repetições
                * Avançado: escolha entre 4 ou 5 séries, e entre 6, 8 ou 10 repetições

                - O campo "suggestedLoad" deve ser incluído **apenas se o exercício tiver carga real (halter, barra, máquina)**.
                - Nunca inclua "suggestedLoad" se o exercício for de peso corporal, prancha ou cardio.
                - Nunca envie o campo "suggestedLoad" com valor 0. Se não houver carga real, não inclua o campo.
                - Sempre valide que o campo "exerciseId" existe no catálogo fornecido. Nunca invente um ID fora da lista.

                Retorne SOMENTE o JSON com este formato:
                {
                  "title": "string",
                  "description": "string",
                  "durationWeeks": 4,
                  "exercises": [
                    {
                      "exerciseId": "string",
                      "name": "string",
                      "muscleGroup": "string",
                      "sets": número,
                      "reps": número,
                      "restSeconds": número,
                      "suggestedLoad": número (somente se for maior que 0 e usar carga real)
                    }
                  ]
                }
                """,
                age, user.gender(), user.height(), user.weight(), user.level(), user.goal(), exerciseCatalog);

        ChatMessage system = new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Você é uma IA de treino que responde apenas com JSON válido e compatível com o modelo esperado.");
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
            System.out.println("\uD83D\uDD0D IA RESPONSE:");
            System.out.println(json);
            return objectMapper.readValue(json, WorkoutPlanGenerated.class);
        } catch (Exception e) {
            System.err.println("\u274C Erro ao fazer parse da IA:");
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar plano com IA", e);
        }
    }
}
