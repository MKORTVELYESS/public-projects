class Tank {
  constructor(
    id,
    name,
    programme,
    country,
    afolu,
    vintage,
    maxQty,
    currentQty,
    contents
  ) {
    this.id = id;
    this.name = name;
    this.programme = programme;
    this.country = country;
    this.afolu = afolu;
    this.vintage = vintage;
    this.maxQty = maxQty;
    this.currentQty = currentQty;
    this.contents = contents;
  }

  load(projObj) {
    this.contents.push(projObj);
    this.currentQty += projObj.qty;
    if (this.currentQty === this.maxQty) {
      Object.freeze(this);
    }
  }
  unload() {}
  remainingCapacity() {
    return this.maxQty - this.currentQty;
  }
  isFull() {
    return this.maxQty === this.currentQty;
  }
  isOverflow() {
    return this.maxQty < this.currentQty;
  }
  isEmpty() {
    return this.currentQty === 0;
  }
  projects() {
    const IDlist = this.contents.map((proj) => {
      return proj.id;
    });
    uniqueIDList = IDlist.map((projID, index, arr) => {
      if (arr.indexOf(projID) === index) {
        return projID;
      }
    });
  }
}

export const tanks = [
  new Tank(1, "ACR_US_TECH_2019", "ACR", "US", "TECH", "2019", 250000, 0, []),
  new Tank(2, "ACR_US_TECH_2021", "ACR", "US", "TECH", "2021", 983472, 0, []),
  new Tank(3, "CAR_US_TECH_2010", "CAR", "US", "TECH", "2010", 9000, 0, []),
  new Tank(4, "VCS_BD_METHR_2019", "VCS", "BD", "METHR", "2019", 83509, 0, []),
];

class Holding {
  constructor(
    symbol,
    vintage,
    programme,
    qty,
    country,
    possibleTanks = [],
    tank = null,
    id = undefined
  ) {
    this.symbol = symbol;
    this.vintage = vintage;
    this.programme = programme;
    this.qty = qty;
    this.country = country;
    this.possibleTanks = possibleTanks;
    this.tank = tank;
    this.id = id;
  }
  loadInto(tankObj) {
    this.tank = tankObj;
    Object.freeze(this);
  }
  isLoaded() {
    return tank !== null;
  }
  isAmbiguos() {
    return this.possibleTanks.length > 1;
  }
  isDefinitive() {
    return this.possibleTanks.length === 1;
  }
  isUncategorizable() {
    return this.possibleTanks.length === 0;
  }
  isAllowed(tank, comparisonMode) {
    switch (comparisonMode) {
      case "strict":
        // code block
        break;
      case "no country":
        // code block
        break;
      case "no vintage":
        // code block
        break;
      case "no vintage no country":
        // code block
        break;
      default:
      // code block
    }
  }
  allowedInTanks(tanksArr) {
    let allowedTanksList = [];

    const getAllowedTanksList = (mode) => {
      allowedTanksList = tanksArr.map((tank) => {
        if (this.isAllowed(tank, mode)) {
          return tank;
        }
      });
      return allowedTanksList;
    };
    //Strict with vintage and country
    if (this.isUncategorizable()) {
      allowedTanksList = getAllowedTanksList("strict");
    }
    //Without country
    if (this.isUncategorizable()) {
      allowedTanksList = getAllowedTanksList("no country");
    }
    //Without vintage
    if (this.isUncategorizable()) {
      allowedTanksList = getAllowedTanksList("no vintage");
    }
    //Without vintage and country

    if (this.isUncategorizable()) {
      allowedTanksList = getAllowedTanksList("no vintage no country");
    }

    return allowedTanksList;
  }
}

export const inventory = [
  new Holding("12488AFD37", "2019", "ACR", 250000, "US"),
  new Holding("12489AFD37", "2021", "ACR", 100000, "US"),
  new Holding("12490AFD37", "2021", "ACR", 200000, "US"),
  new Holding("12491AFD37", "2021", "ACR", 100000, "US"),
  new Holding("12492AFD37", "2021", "ACR", 100000, "US"),
  new Holding("12493AFD37", "2021", "ACR", 183472, "US"),
  new Holding("12496AFD37", "2010", "ACR", 9000, "US"),
  new Holding("12497AFD37", "2019", "VCS", 83509, "BD"),
];
