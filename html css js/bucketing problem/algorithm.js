const tanks = [
  {
    id: 1,
    name: "ACR_US_TECH_2019",
    programme: "ACR",
    country: "US",
    afolu: "TECH",
    vintage: "2019",
    maxQty: 250000,
    currentQty: 0,
    remainingCapacity: null,
    setRemainingCapacity: function () {
      this.remainingCapacity = this.maxQty - this.currentQty;
    },
    contents: [],
  },
  {
    id: 2,
    name: "ACR_US_TECH_2021",
    programme: "ACR",
    country: "US",
    afolu: "TECH",
    vintage: "2021",
    maxQty: 983472,
    currentQty: 0,
    remainingCapacity: null,
    setRemainingCapacity: function () {
      this.remainingCapacity = this.maxQty - this.currentQty;
    },
    contents: [],
  },
  {
    id: 3,
    name: "CAR_US_TECH_2010",
    programme: "CAR",
    country: "US",
    afolu: "TECH",
    vintage: "2010",
    maxQty: 9000,
    currentQty: 0,
    remainingCapacity: null,
    setRemainingCapacity: function () {
      this.remainingCapacity = this.maxQty - this.currentQty;
    },
    contents: [],
  },
  {
    id: 4,
    name: "VCS_BD_METHR_2019",
    programme: "VCS",
    country: "BD",
    afolu: "METHR",
    vintage: "2019",
    maxQty: 83509,
    currentQty: 0,
    remainingCapacity: null,
    setRemainingCapacity: function () {
      this.remainingCapacity = this.maxQty - this.currentQty;
    },
    contents: [],
  },
];

const inventory = [
  {
    symbol: "12488AFD37",
    vintage: "2019",
    programme: "ACR",
    qty: 250000,
    country: "US",
    possibleTanks: [1, 2],
    isInTank: false,
  },
  {
    symbol: "12489AFD37",
    vintage: "2021",
    programme: "ACR",
    qty: 100000,
    country: "US",
    possibleTanks: [2, 3],
    isInTank: false,
  },
  {
    symbol: "12490AFD37",
    vintage: "2021",
    programme: "ACR",
    qty: 200000,
    country: "US",
    possibleTanks: [2, 1],
    isInTank: false,
  },
  {
    symbol: "12491AFD37",
    vintage: "2021",
    programme: "ACR",
    qty: 100000,
    country: "US",
    possibleTanks: [1, 3],
    isInTank: false,
  },
  {
    symbol: "12492AFD37",
    vintage: "2021",
    programme: "ACR",
    qty: 100000,
    country: "US",
    possibleTanks: [2, 4],
    isInTank: false,
  },
  {
    symbol: "12493AFD37",
    vintage: "2021",
    programme: "ACR",
    qty: 183472,
    country: "US",
    possibleTanks: [3, 4],
    isInTank: false,
  },
  {
    symbol: "12494AFD37",
    vintage: "2021",
    programme: "ACR",
    qty: 100000,
    country: "US",
    possibleTanks: [],
    isInTank: false,
  },
  {
    symbol: "12495AFD37",
    vintage: "2021",
    programme: "ACR",
    qty: 200000,
    country: "US",
    possibleTanks: [],
    isInTank: false,
  },
  {
    symbol: "12496AFD37",
    vintage: "2010",
    programme: "CAR",
    qty: 9000,
    country: "US",
    possibleTanks: [],
    isInTank: false,
  },
  {
    symbol: "12497AFD37",
    vintage: "2019",
    programme: "VCS",
    qty: 83509,
    country: "BD",
    possibleTanks: [],
    isInTank: false,
  },
];

function calcReamainingCapacity() {
  tanks.forEach((tank) => {
    tank.setRemainingCapacity();
  });
}

function findPossibleTanks(changeDone) {
  inventory.forEach((project) => {
    tanks.forEach((tank) => {
      const allowedInTank =
        project.isInTank == false &&
        project.vintage === tank.vintage &&
        project.programme == tank.programme &&
        project.qty <= tank.remainingCapacity &&
        project.country == tank.country;
      if (allowedInTank) {
        project.possibleTanks.push(tank.id);
      }
    });

    if (project.possibleTanks.length === 0) {
      tanks.forEach((tank) => {
        const allowedInTankWithoutCountry =
          project.isInTank == false &&
          project.vintage === tank.vintage &&
          project.programme == tank.programme &&
          project.qty <= tank.remainingCapacity;
        if (allowedInTankWithoutCountry) {
          project.possibleTanks.push(tank.id);
        }
      });
    }
  });
  fillTanks(changeDone);
}

function fillTanks(changeDone) {
  inventory.forEach((project) => {
    if (project.possibleTanks.length === 1) {
      const tankToFill = tanks.find((tank) => {
        return tank.id == project.possibleTanks[0];
      });
      tankToFill.contents.push(project.symbol);
      tankToFill.currentQty += project.qty;
      tankToFill.remainingCapacity = tankToFill.maxQty - tankToFill.currentQty;
      project.isInTank = true;
      project.tankId = tankToFill.id;
      changeDone = true;
    }
  });
}

function assignTank() {
  let changeDone;
  calcReamainingCapacity();
  findPossibleTanks(changeDone);
  if (changeDone) {
    findPossibleTanks();
  } else {
    const unassignedProjects = inventory.filter((project) => !project.isInTank);
    const unassginedQty = unassignedProjects.reduce((total, project) => {
      total += project.qty;
    }, 0);
    const unfilledTanks = tanks.filter((tank) => tank.remainingCapacity);
    const unfilledCapacity = tanks.reduce(
      (total, tank) => (total += tank.remainingCapacity),
      0
    );
    console.log("Unassigned projects: ", unassignedProjects);
    console.log("Unassigned qty: ", unassginedQty);
    console.log("Tanks with remaining capacity > 0: ", unfilledTanks);
    console.log("Remaining storage capacity: ", unfilledCapacity);
    return tanks;
  }
}

function combinations(arr, k) {
  if (k === 0) {
    return [[]];
  }

  if (arr.length === 0) {
    return [];
  }

  const element = arr[0];
  const rest = arr.slice(1);

  const combinationsWithoutElement = combinations(rest, k);
  const combinationsWithElement = combinations(rest, k - 1).map((c) => [
    element,
    ...c,
  ]);

  return [...combinationsWithoutElement, ...combinationsWithElement];
}

function generateAllCombinations(n, arr) {
  const result = [];
  for (let k = 1; k <= n; k++) {
    const kCombinations = combinations(arr, k);
    result.push(kCombinations);
  }
  return result;
}

const allCombinations = generateAllCombinations(inventory.length, inventory);
console.log(allCombinations);
calcCombinationTotals;

function calcCombinationTotals(allCombinations) {
  let newArray = [];
  allCombinations.forEach((item) => {
    newArray.push(...item);
  });
  console.log(newArray);
  const combinationsWithTotals = newArray.map((combination) => {
    const combinationTotal = combination.reduce((combinedQty, project) => {
      return (combinedQty += project.qty);
    }, 0);
    return {
      combinationTotal,
      combination,
    };
  });
  return combinationsWithTotals;
}

function getAllPossibleProjectsForTank(id) {
  return inventory.filter((project) => {
    return project.possibleTanks.indexOf(id) >= 0;
  });
}

function getTotalQtyOfProjectList(list) {
  return list.reduce((sum, project) => {
    return (sum += project.qty);
  }, 0);
}

console.log(getAllPossibleProjectsForTank(1));
console.log(getTotalQtyOfProjectList(getAllPossibleProjectsForTank(1)));
function overfillTank(tank, totalQty) {
  
}
