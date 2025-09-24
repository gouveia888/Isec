package Aula_7_3_2025.hangman.model;

public class HangmanGameDictionary {
    private static final String[] words = {
            "Eletrocardiograma", "ESTETOSCOPIO", "SERINGA", "AGULHA", "BISTURI", "PLACA",
            "MORFINA", "LARINGOSCOPIO", "TERMOMETRO", "ULTRASSOM", "INJETOR",
            "CATHETER", "MONITOR", "ECMO", "OXÍMETRO", "RESUSCITADOR", "NEBULIZADOR",
            "ANTIBIOTICO", "ENFERMEIRO", "MEDICO", "FERIMENTO", "TRAUMA", "CIRURGIA",
            "DIAGNOSTICO", "INFERIOR", "ANESTESIA", "SUTURA", "INFEÇÃO", "GESSO",
            "TRATAMENTO", "ESTABILIZAÇAO", "URGENCIA", "CONDIÇAO", "FERRO", "NEUROLOGIA",
            "HEMOGLOBINA", "PLASMA", "EXAME", "REANIMAÇAO", "RETORNO", "VACINA", "IMUNIZAÇAO",
            "PRESCRIÇAO", "INFUSAO", "RAIOX", "PULSO", "REFLEXO", "CUIDADO", "BANDAID",
            "VERDADE", "ATENDIMENTO", "HOSPITAL", "MATERIAIS", "INTUBAÇAO", "LEITO",
            "CATETERISMO", "SISTEMA", "TECIDOS", "HIDRATAÇAO", "GASOMETRIA", "TERAPIA",
            "INSULINA", "CUIDADO", "DIABETES", "VITAMINA", "FISIOTERAPIA", "EXAME", "PRESCRIÇAO"
    };

    private HangmanGameDictionary() {
    }

    public static int getNumWords() {
        return words.length;
    }

    public static String getWord(int index) {
        if (index < 0 || index >= words.length)
            return null;
        return words[index];
    }
}
