package Vehicles;

import java.util.regex.Pattern;

/**
 * Provides registration number of Vehicles and hash code method
 */
public class Vehicle {
  private static final Pattern VALID_PLATE = Pattern.compile("^[A-ZÅÄÖ]{3}[0-9]{2}[A-Z0-9]$|^[A-ZÅÄÖ]{3}[0-9]{3}$");

  private final String regNumber;
  private final String meta;

  public Vehicle(String regNumber) {
    this(regNumber, null);
  }

  public Vehicle(String regNumber, String meta) {
    if (regNumber == null) throw new NullPointerException("regNumber can not be null");
    String  norm = regNumber.trim().toUpperCase();

    if (!VALID_PLATE.matcher(norm).matches()) {
      throw new IllegalArgumentException("Invalid registration format: \" + regNumber");
    }
    this.regNumber = norm;
    this.meta = meta;
  }

  public String regNumber() {
    return regNumber;
  }

  public String meta() {
    return meta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Vehicle)) return false;
    Vehicle that = (Vehicle) o;
    return regNumber.equals(that.regNumber);
  }

  @Override
  public int hashCode() {
    int hash = 17;
    for (int i = 0; i < regNumber.length(); i++) {
      hash = 31 * hash + regNumber.charAt(i);
    }
    return hash;
  }

}
