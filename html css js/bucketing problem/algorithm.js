import { tanks } from "./data.js";
import { inventory } from "./data.js";

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
function overfillTank(tank, totalQty) {}
