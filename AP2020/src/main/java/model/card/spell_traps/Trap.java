package model.card.spell_traps;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import controller.database.ReadAndWriteDataBase;
import controller.database.CSVInfoGetter;
import model.card.Card;
import model.card.CardType;
import model.card.FeatureWrapper;

import java.util.ArrayList;
@JsonIgnoreProperties({"description","cardType","cardID","trapType","limit"})
public class Trap extends Card {
    private TrapType trapType;
    private Limitation limit;

    public Trap(String cardName) {
        setAttributesByName(cardName);
    }

    public Trap(){

    }

    @JsonIgnore
    public Limitation getLimit(){
        return this.limit;
    }

    @JsonIgnore
    public TrapType getTrapType(){
        return this.trapType;
    }

    @Override
    public void setCardName(String cardName) {
        this.cardName = cardName;
        setAttributesByName(cardName);
        FeatureWrapper wrapper = ReadAndWriteDataBase.getCardFeaturesByName(cardName);
        this.features = wrapper.features;
    }

    public void setAttributesByName(String cardName) {
        ArrayList<String> temp = CSVInfoGetter.trapAndSpellReadFromCSV(cardName);
        if (temp == null || temp.size() != 4) return;
        trapType = CSVInfoGetter.getTrapType(temp.get(1));
        this.cardName = cardName;
        description = temp.get(2);
        limit = CSVInfoGetter.getLimitation(temp.get(3));
        cardType = CardType.TRAP;
        features = new ArrayList<>();
        FeatureWrapper wrapper = ReadAndWriteDataBase.getCardFeaturesByName(cardName);
        this.features = wrapper.features;
        this.syncedEffect = wrapper.getEffectMap();
        this.syncedEffect = wrapper.getEffectMap();

    }

    public String toString(){
        StringBuilder temp = new StringBuilder();
        temp.append("Name: " + cardName + "\n");
        temp.append("Trap\n");
        temp.append("Type: " + trapType + "\n");
        temp.append("Description: " + description);
        return temp.toString();
    }
}
