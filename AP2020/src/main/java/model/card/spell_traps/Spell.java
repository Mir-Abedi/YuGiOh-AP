package model.card.spell_traps;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import controller.database.ReadAndWriteDataBase;
import controller.database.CSVInfoGetter;
import model.card.Card;
import model.card.CardType;
import model.card.FeatureWrapper;

import java.util.ArrayList;

@JsonIgnoreProperties({"description","cardType","cardID","spellType","limit"})
public class Spell extends Card {
    private SpellType spellType;
    @JsonIgnore
    private Limitation limit;

    public Spell(String cardName) {
       setAttributesByName(cardName);

    }

    public Spell(){

    }

    @JsonIgnore
    public Limitation getLimit(){
        return this.limit;
    }


    @JsonIgnore
    public SpellType getSpellType(){
        return this.spellType;
    }

    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append("Name: " + cardName + "\n");
        temp.append("Spell\n");
        temp.append("Type: " + spellType + "\n");
        temp.append("Description: " + description);
        return temp.toString();
    }
    @Override
    public void setCardName(String cardName) {
        this.cardName = cardName;
        setAttributesByName(cardName);
    }

    public void setAttributesByName(String cardName) {
        ArrayList<String> temp = CSVInfoGetter.trapAndSpellReadFromCSV(cardName);
        if (temp == null || temp.size() != 4) return;
        spellType = CSVInfoGetter.getSpellType(temp.get(1));
        this.cardName = cardName;
        description = temp.get(2);
        limit = CSVInfoGetter.getLimitation(temp.get(3));
        cardType = CardType.SPELL;
        this.features = new ArrayList<>();
        FeatureWrapper wrapper = ReadAndWriteDataBase.getCardFeaturesByName(cardName);
        this.features = wrapper.features;
        syncedEffect = wrapper.getEffectMap();
    }
}
