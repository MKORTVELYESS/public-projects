import { tanks } from "./data.js";
import { inventory } from "./data.js";
import { getCombinations } from "./getCombinations.js";
function calcReamainingCapacity() {
  tanks.forEach((tank) => {
    tank.setRemainingCapacity();
  });
}
//
function findPossibleTanks(changeDone) {
  inventory.forEach((project) => {
    project.allowedInTanks(tanks);
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
let allCombinations = getCombinations(inventory.length, inventory);
console.log(allCombinations);
const tankCombination = allCombinations.filter((item) => {
  return Math.abs(item.combinationTotal - 373182) < 500;
});
console.log(tankCombination);
