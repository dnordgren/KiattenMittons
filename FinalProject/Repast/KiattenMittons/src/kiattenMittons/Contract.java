package kiattenMittons;

public class Contract {
    private Team team;
    private double value;
    private int yearsRemaining;

    public Contract() {
    	this.team = null;
    }

    public Contract(Team team, double value, int years) {
        this.team = team;
        this.value = value;
        this.yearsRemaining = years;
    }

    /**
     * Sets values for an updated contract.
     * @param team
     * @param years
     * @param value
     */
    public void signWithTeam(Team team, int years, double value) {
        this.team = team;
        this.yearsRemaining = years;
        this.value = value;
    }

    /**
     * Updates the years remaining on the contract.
     * If the contract has expired, the team is set to null.
     */
    public void updateYearsRemaining() {
        if(0 == --this.yearsRemaining) {
            this.team = null;
        }
    }

    /**
     * Gets the team associated with the contract.
     * @return contract team
     */
    public Team getSignedTeam() {
        return this.team;
    }

    /**
     * Gets the yearly salary of the contract.
     * @return yearly value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Gets the years remaining on the contract.
     * @return years remaining
     */
    public int getYearsRemaining() {
        return this.yearsRemaining;
    }
}