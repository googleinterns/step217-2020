export default function objectEquals(object1, object2) {
  if ((object1 == undefined && object2 != undefined) ||
      (object2 == undefined && object1 != undefined))
    return false;
  if ((object1 === undefined && object2 === undefined) ||
      (object1 === null && object2 === null))
    return true;
  const keys1 = Object.keys(object1);
  const keys2 = Object.keys(object2);

  if (keys1.length !== keys2.length) {
    return false;
  }

  for (let key of keys1) {
    if (object1[key] !== object2[key]) {
      return false;
    }
  }

  return true;
 } 