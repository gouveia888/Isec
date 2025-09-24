function checkNeighbours(squaresArray, width, height, i, j) {
  if (i > 0) {
    // Se estiver na 2ª linha ou abaixo, pesquisa as 3 quadrículas adjacentes acima
    for (let k = -1; k < 2; k++) {
      const aux = squaresArray[i - 1][j + k];
      if (j + k >= 0 && j + k < width) {
        if (aux["name"] === "0" && aux["isFlipped"] === "0") {
          aux["isFlipped"] = "1";
          checkNeighbours(squaresArray, width, height, i - 1, j + k);
        } else {
          aux["isFlipped"] = "1";
        }
      }
    }
  }
  if (i < height - 1) {
    // Se estiver na penúltima linha ou acima, pesquisa as 3 quadrículas adjacentes abaixo
    for (let k = -1; k < 2; k++) {
      const aux = squaresArray[i + 1][j + k];
      if (j + k >= 0 && j + k < width) {
        if (aux["name"] === "0" && aux["isFlipped"] === "0") {
          aux["isFlipped"] = "1";
          checkNeighbours(squaresArray, width, height, i + 1, j + k);
        } else {
          aux["isFlipped"] = "1";
        }
      }
    }
  }
  if (j > 0) {
    // Se estiver na 2ª coluna ou à direita, pesquisa a quadrícula adjacente à esquerda
    const aux = squaresArray[i][j - 1];
    if (aux["name"] === "0" && aux["isFlipped"] === "0") {
      aux["isFlipped"] = "1";
      checkNeighbours(squaresArray, width, height, i, j - 1);
    } else {
      aux["isFlipped"] = "1";
    }
  }
  if (j < width - 1) {
    // Se estiver na penúltima coluna ou à esquerda, pesquisa a quadrícula adjacente à direita
    const aux = squaresArray[i][j + 1];
    if (aux["name"] === "0" && aux["isFlipped"] === "0") {
      aux["isFlipped"] = "1";
      checkNeighbours(squaresArray, width, height, i, j + 1);
    } else {
      aux["isFlipped"] = "1";
    }
  }
}

export default checkNeighbours;
