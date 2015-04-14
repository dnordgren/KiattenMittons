package kiattenMittons;

public class Contract {
    private int yearsRemaining;
    private double value;
    private Team team;

    public Contract() {
    	this.team = null;
    }
    
    public int getYearsRemaining() {
        return this.yearsRemaining;
    }

    public void updateYearsRemaining() {
        if(0 == --this.yearsRemaining) {
            this.team = null;
        }
    }

    public double getValue() {
        return this.value;
    }

    public void signWithTeam(Team team, int years, double value) {
        this.team = team;
        this.yearsRemaining = years;
        this.value = value;
    }
    
    public Team getSignedTeam() {
    	return this.team;
    }
}