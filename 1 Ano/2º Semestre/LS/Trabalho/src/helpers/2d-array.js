function finalArrayGenerator(squaresArray, level) {
  let width, height, twoDArray = [];

  switch (level) {
    case "1":
      width = height = 9;
      break;
    case "2":
      width = height = 16;
      break;
    case "3":
      width = 30;
      height = 16;
      break;
    default:
      width = height = 9;
      break;
  }

  for (let i = 0; i < height; i++) {
    twoDArray[i] = [];
    for (let j = 0; j < width; j++) {
      let aux = squaresArray.pop();
      twoDArray[i][j] = aux;
    }
  }

  for (let i = 0; i < height; i++) {
    for (let j = 0; j < width; j++) {
      let square = twoDArray[i][j];
      let counter = 0;

      if (square["name"] !== "bomb") {
        if (i > 0) {
          // Se estiver na 2ª linha ou abaixo, pesquisa as 3 quadrículas adjacentes acima
          for (let k = -1; k < 2; k++) {
            if (
              j + k >= 0 &&
              j + k < width &&
              twoDArray[i - 1][j + k]["name"] === "bomb"
            )
              counter++;
          }
        }
        if (i < height - 1) {
          // Se estiver na penúltima linha ou acima, pesquisa as 3 quadrículas adjacentes abaixo
          for (let k = -1; k < 2; k++) {
            if (
              j + k >= 0 &&
              j + k < width &&
              twoDArray[i + 1][j + k]["name"] === "bomb"
            )
              counter++;
          }
        }
        if (j > 0) {
          // Se estiver na 2ª coluna ou à direita, pesquisa a quadrícula adjacente à esquerda
          if (twoDArray[i][j - 1]["name"] === "bomb") counter++;
        }
        if (j < width - 1) {
          // Se estiver na penúltima coluna ou à esquerda, pesquisa a quadrícula adjacente à direita
          if (twoDArray[i][j + 1]["name"] === "bomb") counter++;
        }

        square["name"] = counter.toString();
      }
    }
  }

  return twoDArray;
}

export default finalArrayGenerator;
