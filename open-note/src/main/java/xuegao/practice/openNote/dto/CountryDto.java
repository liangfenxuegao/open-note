package xuegao.practice.openNote.dto;

import xuegao.practice.openNote.entity.Continent;
import xuegao.practice.openNote.entity.Country;

public class CountryDto extends Country {
    private Continent continent;//洲

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }
}
