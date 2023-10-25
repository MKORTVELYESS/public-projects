export function getCombinations(n, arr) {
  let result = [];
  for (let k = 1; k <= n; k++) {
    const kCombinations = combinations(arr, k);
    result.push(kCombinations);
  }

  result = calcCombinationTotals(result);
  return result;
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
