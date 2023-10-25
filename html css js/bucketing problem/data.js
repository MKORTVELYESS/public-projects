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
  new Tank(1, "VCS_CN_METHR_2021", "VCS", "CN", "METHR", "2021", 373182, 0, []),
  new Tank(
    2,
    "VCS_CN_METHR_2021_GEO",
    "VCS",
    "CN",
    "METHR",
    "2021",
    610492,
    0,
    []
  ),
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
  new Holding("1", "2021", "VCS", 105312, "CN"),
  new Holding("2", "2021", "VCS", 190000, "CN"),
  new Holding("3", "2021", "VCS", 181438, "CN"),
  new Holding("4", "2021", "VCS", 140851, "CN"),
  new Holding("5", "2021", "VCS", 50000, "CN"),
  new Holding("6", "2021", "VCS", 167202, "CN"),
  new Holding("7", "2021", "VCS", 77148, "CN"),
  new Holding("8", "2021", "VCS", 71723, "CN"),
];
