; implementar movimentos < carregador e liquidos
; IMPLEMENTAR VARIAVEL DE MEMORIA COM OBJETIVO
breed [robo robos]
robo-own[energia localY localX cap-atual]
globals [robo-eficiente robo-autoeficiente]


to Setup
  clear-all
  reset-ticks

  ;local de depósito do lixo
  ask patches with [pxcor >= 16 and pxcor <= 17 and pycor >= 16 and pycor <= 17] [
    set pcolor green
  ]

  Setup-patches

  ;criar robos
  create-robo num_robos
  ask robo [

    set localX 16
    set localY 17


  ifelse robo-autoeficiente?
    [
      let carreg one-of patches with [pcolor = blue and not any? robo-here]
      if carreg != nobody [
        move-to carreg
        set localX pxcor
        set localY pycor
      ]
    ]
    [
      move-to one-of patches with [pcolor = black and not any? robo-here]
    ]






    set color violet
    set heading one-of [0 90 180 270]
    set shape "arrow"
    set cap-atual 0
    set energia bateria

  ]

end

to Setup-patches

  let num_lixo ((max-pxcor + 1) * (max-pycor + 1)) * lixo / 100   ; Os patches de lixo

  ask n-of num_lixo patches with [pcolor != green]     ;celulas aleatorias
  [
     set pcolor red
  ]

  ask n-of num_carg patches with [pcolor != red and pcolor != green] [
    set pcolor blue
  ]

  ask n-of objetos patches with [pcolor != red and pcolor != blue and pcolor != green] [
    set pcolor white
  ]

  ask n-of agua patches with [pcolor != red and pcolor != blue and pcolor != white and pcolor != green] [
    set pcolor cyan
  ]
end


to Go

    let fim ticks
    if count turtles = 0 or fim > 10000 or count patches with [pcolor = red] = 0
    [stop]
  ifelse Diagonal[MoveRobos-diagonal]
  [MoveRobos]



  ask robo [
    Recolhe
  ]
  tick
end

to Recolhe

  ifelse robo-eficiente?
  [
    if pcolor = red and energia > low_batery and cap-atual < capacidade[
      set pcolor black
      set cap-atual cap-atual + 1
    ]

    if pcolor = cyan and energia > low_batery and cap-atual < capacidade - 1[
      set pcolor black
      set cap-atual cap-atual + 2
    ]
  ]
  [
    if pcolor = red and energia > low_batery and cap-atual < capacidade[
      set pcolor black
      set cap-atual cap-atual + 1
    ]

    if pcolor = cyan [
       set pcolor white;
      die
    ]
  ]
end

to-report Distancia-carregador
show "Robo"

  ;; Calculo intermédio
  let x-min pxcor - localX
  let y-min pycor - localY

  ;;Calculo
  let pos-min abs(x-min) + abs(y-min) + 1
show pos-min
  report pos-min

end

to procura-carregador

  ifelse localX = pxcor and localY = pycor[
    let atual ticks
    let inicial ticks
    while [inicial < atual + time_charge]
    [
      show "A CARREGAR"
      set inicial inicial + 1
      wait 1
    ]
    set color violet
    set energia bateria
  ]
  [
    ifelse any? neighbors4 with [pcolor = blue and not any? robo-here]
    [
      face one-of neighbors4 with [pcolor = blue]
      set energia energia - 1
      fd 1
    ]
    [
      ifelse xcor >= max-pxcor or xcor <= min-pxcor or ycor >= max-pycor or ycor <= min-pycor and not any? robo-here and [pcolor] of patch-ahead 1 != white and patch-ahead 1 = nobody [

      ifelse (pycor < localY and heading = 0) or
         (pxcor < localX and heading = 90) or
         (pycor > localY and heading = 180) or
         (pxcor > localX and heading = 270) [
        fd 1
        set energia energia - 1
      ]
       [
        ifelse (pycor = localY and heading = 0 and pxcor < localX) or
           (pycor = localY and heading = 180 and pxcor > localX) or
           (pxcor = localX and heading = 90 and pycor > localY) or
           (pxcor = localX and heading = 270 and pycor < localY) [
          rt 90
        ]
         [
          if (pycor = localY and heading = 0 and pxcor > localX) or
             (pycor = localY and heading = 180 and pxcor < localX) or
             (pxcor = localX and heading = 90 and pycor < localY) or
             (pxcor = localX and heading = 270 and pycor > localY) or
             (pxcor = localX and heading = 180 and pycor < localY) [
            lt 90
          ]
          ]
        ]
      ]

  [
    ifelse patch-ahead 1 = nobody or [pcolor] of patch-ahead 1 = white
        [
          rt 90
        ]
        [
          fd 1
          set energia energia - 1
        ]
  ]
 ]
]



end



to guarda-posicao
  ;; Verifica se há carregadores
  if any? neighbors4 with [pcolor = blue] [

      ;; Verifica Frente
      if patch-ahead 1 != nobody and [pcolor] of patch-ahead 1 = blue [
        ;; Acede ao patch-ahead 1
        let carregador patch-ahead 1
        set localY [pycor] of carregador
        set localX [pxcor] of carregador
      ]
      ;; Verifica Esquerda
      if patch-left-and-ahead 90 1 != nobody and [pcolor] of patch-left-and-ahead 90 1 = blue [
        let carregador patch-left-and-ahead 90 1
        set localX [pxcor] of carregador
        set localY [pycor] of carregador
      ]
      ;; Verifica Atrás
      if patch-left-and-ahead 180 1 != nobody and [pcolor] of patch-left-and-ahead 180 1 = blue [
        let carregador patch-left-and-ahead 180 1
        set localY [pycor] of carregador
        set localX [pxcor] of carregador
      ]
      ;; Verifica Direita
      if patch-right-and-ahead 90 1 != nobody and [pcolor] of patch-right-and-ahead 90 1 = blue [
        let carregador patch-right-and-ahead 90 1
        set localX [pxcor] of carregador
        set localY [pycor] of carregador
      ]
  ]
end
to fim-vida

    if energia = 0 or energia < 0
     [
       set energia 0
       set pcolor white
       show "Morte"
       die
     ]

end



to auto-suficiente
  if robo-autoeficiente? [
    let distancia Distancia-carregador
    if distancia > 0 and distancia >= energia [
       procura-carregador ; Verifica se é necessário procurar um carregador
      ; Mover para o patch correspondente a localX e localY
      move-to patch localX localY  ; Usar patch para mover o robô
      if energia > 0 [   ; Verifica se a energia é positiva antes de reduzir
        set energia energia - 1 ; Reduzir energia após o movimento
      ]
     procura-carregador ; Verifica se é necessário procurar um carregador
    ]
  ]
end

to vizinhaca

  if any? turtles-on neighbors4
  [
    ifelse any? turtles-on patch-ahead 1
    [
      let vizinho one-of turtles-on patch-ahead 1
      let v_localX [localX] of vizinho
      let v_localY [localY] of vizinho
      if abs(pxcor - localX) < abs(pxcor - v_localX) and abs(pycor - localY) < abs(pycor - v_localY)
      [
        set localX v_localX
        set localY v_localY
      ]
    ]
    [
      ifelse any? turtles-on patch-left-and-ahead 90 1
      [
        let vizinho one-of turtles-on patch-left-and-ahead 90 1
        let v_localX [localX] of vizinho
        let v_localY [localY] of vizinho
        if abs(pxcor - localX) < abs(pxcor - v_localX) and abs(pycor - localY) < abs(pycor - v_localY)
        [
          set localX v_localX
          set localY v_localY
        ]
      ]
      [
        ifelse any? turtles-on patch-left-and-ahead 180 1
        [
          let vizinho one-of turtles-on patch-left-and-ahead 180 1
          let v_localX [localX] of vizinho
          let v_localY [localY] of vizinho
          if abs(pxcor - localX) < abs(pxcor - v_localX) and abs(pycor - localY) < abs(pycor - v_localY)
          [
            set localX v_localX
            set localY v_localY
          ]
        ]
        [
          if any? turtles-on patch-right-and-ahead 90 1
          [
            let vizinho one-of turtles-on patch-right-and-ahead 90 1
            let v_localX [localX] of vizinho
            let v_localY [localY] of vizinho
            if abs(pxcor - localX) < abs(pxcor - v_localX) and abs(pycor - localY) < abs(pycor - v_localY)
            [
              set localX v_localX
              set localY v_localY
            ]
          ]
        ]
      ]
    ]
  ]

end
to MoveRobos
  ask robo [
    set heading one-of [0 90 180 270]
    let movimentos Distancia-carregador
    show (word "Movimentos necessários: " movimentos)
    show (word "Energia restante: " energia)
    show (word "Posiçao atual " pxcor " " pycor)
    show (word "Carregador mais proximo " localX " " localY)
    guarda-posicao
    fim-vida
    auto-suficiente

    ifelse energia <= low_batery   ;deixar so o igual
       [
         set color yellow
         show " A procura de carregador!!"
         procura-carregador
       ]
      ; verifica se tem algum vermelho (lixo) ao redor dele se sim vai para ele
      [
        ifelse cap-atual < capacidade
          [
            ifelse any? neighbors4 with [pcolor = red or pcolor = cyan and not any? robo-here]
              [
                face one-of neighbors4 with [pcolor = red or pcolor = cyan]
                fd 1
                set energia energia - 1
              ]
              [
                ifelse patch-ahead 1 = nobody or [pcolor] of patch-ahead 1 = white or [pcolor] of patch-ahead 1 = black or [pcolor] of patch-ahead 1 = blue
                  [
                    ifelse any? neighbors4 with [(pcolor = black or pcolor = blue) and not any? robo-here]
                    [
                      let mov one-of neighbors4 with [pcolor = black]
                      if mov != nobody
                      [
                      fd 1
                      set energia energia - 1
                      ]
                    ]
                    [rt 90]
                 ]
                 [
                   ifelse random 101 <= 50
                     [rt 90]   ;;roda direita com 5%
                     [lt 90]   ;; roda esqueda com 5%
                 ]

              ]
          ]

      [
       ifelse cap-atual = capacidade
        [
         depositar
        ]
        [
         set energia 0
         set pcolor white
         show "Subcarga"
         die
        ]
      ]
    ]
 ]
end

to depositar
  ; Verifica se o robô está na zona verde
  if pcolor = green [
    set cap-atual 0
    let atual ticks
    let inicial ticks
    while [inicial < atual + time_desp]
    [
      show "A DESPEJAR"
      set inicial inicial + 1
      wait 1
    ]

  ]
  ; Verifica se tem algum obstáculo à frente
  ifelse patch-ahead 1 = nobody or [pcolor] of patch-ahead 1 = white  [
    ; Se tiver obstáculo, escolhe uma direção aleatória que não seja obstáculo
    ifelse random 101 <= 90 ;nada a frente
    [ifelse any? neighbors4 with [pcolor != white]
      [face one-of neighbors4 with [pcolor != white]
        fd 1
        set energia energia - 1]
      [rt 90]
    ]
    [ifelse random 101 <= 5
      [rt 90]   ;;roda direita com 5%
      [lt 90]   ;; roda esqueda com 5%
    ]
  ] [
    ; Se não tiver obstáculo, move-se para frente
    fd 1
    set energia energia - 1
  ]
end






to MoveRobos-diagonal
  ask robo [
    set heading one-of [0 90 180 270]
    let movimentos Distancia-carregador
    show (word "Movimentos necessários: " movimentos)
    show (word "Energia restante: " energia)
    show (word "Posiçao atual " pxcor " " pycor)
    show (word "Carregador mais proximo " localX " " localY)
    guarda-posicao
    fim-vida
    auto-suficiente

    ifelse energia <= low_batery   ;deixar so o igual
       [
         set color yellow
         show " A procura de carregador!!"
         procura-carregador
       ]
      ; verifica se tem algum vermelho (lixo) ao redor dele se sim vai para ele
      [
        ifelse cap-atual < capacidade
          [
            ifelse any? neighbors with [pcolor = red or pcolor = cyan and not any? robo-here]
              [
                face one-of neighbors with [pcolor = red or pcolor = cyan]
                fd 1
                set energia energia - 1
              ]
              [
                ifelse patch-ahead 1 = nobody or [pcolor] of patch-ahead 1 = white or [pcolor] of patch-ahead 1 = black or [pcolor] of patch-ahead 1 = blue
                  [
                    ifelse any? neighbors with [(pcolor = black or pcolor = blue) and not any? robo-here]
                    [
                      face one-of neighbors with [pcolor = black]
                      fd 1
                      set energia energia - 1
                    ]
                    [rt 90]
                 ]
                 [
                   ifelse random 101 <= 50
                     [rt 90]   ;;roda direita com 5%
                     [lt 90]   ;; roda esqueda com 5%
                 ]

              ]
          ]

      [
       ifelse cap-atual = capacidade
        [
         depositar
        ]
        [
         set energia 0
         set pcolor white
         show "Subcarga"
         die
        ]
      ]
    ]
 ]
end
@#$#@#$#@
GRAPHICS-WINDOW
243
10
680
448
-1
-1
13.0
1
10
1
1
1
0
0
0
1
0
32
0
32
0
0
1
ticks
30.0

SLIDER
40
44
212
77
lixo
lixo
0
60
20.0
1
1
NIL
HORIZONTAL

SLIDER
41
87
213
120
num_robos
num_robos
0
5
5.0
1
1
NIL
HORIZONTAL

BUTTON
394
465
457
498
GO
GO
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
478
466
541
499
Setup
Setup
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
41
125
213
158
num_carg
num_carg
0
5
5.0
1
1
NIL
HORIZONTAL

SLIDER
40
166
212
199
Objetos
Objetos
0
100
50.0
1
1
NIL
HORIZONTAL

SLIDER
40
215
212
248
capacidade
capacidade
1
100
100.0
1
1
NIL
HORIZONTAL

SLIDER
39
293
211
326
low_batery
low_batery
1
100
15.0
1
1
NIL
HORIZONTAL

SLIDER
39
338
211
371
time_charge
time_charge
1
50
1.0
1
1
ticks
HORIZONTAL

MONITOR
774
206
843
251
Lixo
count patches with [pcolor = red]
17
1
11

MONITOR
849
207
920
252
Obstáculos
count patches with [pcolor = white]
17
1
11

SLIDER
852
82
1024
115
agua
agua
0
100
0.0
1
1
NIL
HORIZONTAL

SWITCH
706
83
845
116
robo-eficiente?
robo-eficiente?
1
1
-1000

MONITOR
702
205
767
250
Limpos
count patches with [pcolor = black and not any? robo-here]
17
1
11

SLIDER
39
380
211
413
time_desp
time_desp
1
50
1.0
1
1
ticks
HORIZONTAL

SWITCH
704
124
868
157
robo-autoeficiente?
robo-autoeficiente?
1
1
-1000

SLIDER
40
254
212
287
bateria
bateria
1
100
50.0
1
1
NIL
HORIZONTAL

MONITOR
704
255
921
300
Agua
count patches with [pcolor = cyan\n]
17
1
11

SWITCH
705
40
808
73
Diagonal
Diagonal
0
1
-1000

TEXTBOX
93
19
243
37
Modelo Base
10
0.0
1

TEXTBOX
786
178
821
196
Dados
10
0.0
1

TEXTBOX
705
19
855
37
Melhorias
10
0.0
1

@#$#@#$#@
## WHAT IS IT?

(a general understanding of what the model is trying to show or explain)

## HOW IT WORKS

(what rules the agents use to create the overall behavior of the model)

## HOW TO USE IT

(how to use the model, including a description of each of the items in the Interface tab)

## THINGS TO NOTICE

(suggested things for the user to notice while running the model)

## THINGS TO TRY

(suggested things for the user to try to do (move sliders, switches, etc.) with the model)

## EXTENDING THE MODEL

(suggested things to add or change in the Code tab to make the model more complicated, detailed, accurate, etc.)

## NETLOGO FEATURES

(interesting or unusual features of NetLogo that the model uses, particularly in the Code tab; or where workarounds were needed for missing features)

## RELATED MODELS

(models in the NetLogo Models Library and elsewhere which are of related interest)

## CREDITS AND REFERENCES

(a reference to the model's URL on the web if it has one, as well as any other necessary credits, citations, and links)
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

sheep
false
15
Circle -1 true true 203 65 88
Circle -1 true true 70 65 162
Circle -1 true true 150 105 120
Polygon -7500403 true false 218 120 240 165 255 165 278 120
Circle -7500403 true false 214 72 67
Rectangle -1 true true 164 223 179 298
Polygon -1 true true 45 285 30 285 30 240 15 195 45 210
Circle -1 true true 3 83 150
Rectangle -1 true true 65 221 80 296
Polygon -1 true true 195 285 210 285 210 240 240 210 195 210
Polygon -7500403 true false 276 85 285 105 302 99 294 83
Polygon -7500403 true false 219 85 210 105 193 99 201 83

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

wolf
false
0
Polygon -16777216 true false 253 133 245 131 245 133
Polygon -7500403 true true 2 194 13 197 30 191 38 193 38 205 20 226 20 257 27 265 38 266 40 260 31 253 31 230 60 206 68 198 75 209 66 228 65 243 82 261 84 268 100 267 103 261 77 239 79 231 100 207 98 196 119 201 143 202 160 195 166 210 172 213 173 238 167 251 160 248 154 265 169 264 178 247 186 240 198 260 200 271 217 271 219 262 207 258 195 230 192 198 210 184 227 164 242 144 259 145 284 151 277 141 293 140 299 134 297 127 273 119 270 105
Polygon -7500403 true true -1 195 14 180 36 166 40 153 53 140 82 131 134 133 159 126 188 115 227 108 236 102 238 98 268 86 269 92 281 87 269 103 269 113

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270
@#$#@#$#@
NetLogo 6.4.0
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
<experiments>
  <experiment name="Tabela 1" repetitions="15" runMetricsEveryStep="false">
    <setup>setup</setup>
    <go>go</go>
    <metric>count patches with [pcolor = red]</metric>
    <metric>ticks</metric>
    <enumeratedValueSet variable="num_carg">
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Objetos">
      <value value="100"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="bateria">
      <value value="50"/>
      <value value="100"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-autoeficiente?">
      <value value="false"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="num_robos">
      <value value="2"/>
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="low_batery">
      <value value="30"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_desp">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-eficiente?">
      <value value="false"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="agua">
      <value value="50"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="lixo">
      <value value="20"/>
      <value value="40"/>
      <value value="60"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_charge">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="capacidade">
      <value value="100"/>
    </enumeratedValueSet>
  </experiment>
  <experiment name="Tabela 2" repetitions="15" runMetricsEveryStep="false">
    <setup>setup</setup>
    <go>go</go>
    <metric>count patches with [pcolor = red or pcolor = cyan]</metric>
    <metric>ticks</metric>
    <enumeratedValueSet variable="num_carg">
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Objetos">
      <value value="100"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="bateria">
      <value value="100"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-autoeficiente?">
      <value value="false"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="num_robos">
      <value value="2"/>
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="low_batery">
      <value value="30"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_desp">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-eficiente?">
      <value value="true"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="agua">
      <value value="50"/>
      <value value="100"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="lixo">
      <value value="20"/>
      <value value="40"/>
      <value value="60"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_charge">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="capacidade">
      <value value="100"/>
    </enumeratedValueSet>
  </experiment>
  <experiment name="Tabela 3" repetitions="15" runMetricsEveryStep="false">
    <setup>setup</setup>
    <go>go</go>
    <metric>count patches with [pcolor = red or pcolor = cyan]</metric>
    <metric>ticks</metric>
    <enumeratedValueSet variable="num_carg">
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Objetos">
      <value value="100"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="bateria">
      <value value="50"/>
      <value value="100"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-autoeficiente?">
      <value value="true"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="num_robos">
      <value value="2"/>
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="low_batery">
      <value value="30"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_desp">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-eficiente?">
      <value value="true"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="agua">
      <value value="50"/>
      <value value="100"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="lixo">
      <value value="20"/>
      <value value="40"/>
      <value value="60"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_charge">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="capacidade">
      <value value="100"/>
    </enumeratedValueSet>
  </experiment>
  <experiment name="experiment" repetitions="15" runMetricsEveryStep="true">
    <setup>setup</setup>
    <go>go</go>
    <metric>count patches with [pcolor = red or pcolor = cyan]</metric>
    <metric>ticks</metric>
    <enumeratedValueSet variable="num_carg">
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Objetos">
      <value value="50"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="bateria">
      <value value="50"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-autoeficiente?">
      <value value="false"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="num_robos">
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="low_batery">
      <value value="15"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_desp">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-eficiente?">
      <value value="false"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="agua">
      <value value="0"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="lixo">
      <value value="20"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Diagonal">
      <value value="true"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_charge">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="capacidade">
      <value value="100"/>
    </enumeratedValueSet>
  </experiment>
  <experiment name="Tabela 4" repetitions="15" runMetricsEveryStep="false">
    <setup>setup</setup>
    <go>go</go>
    <metric>count patches with [pcolor = red]</metric>
    <metric>ticks</metric>
    <enumeratedValueSet variable="num_carg">
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Objetos">
      <value value="50"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="bateria">
      <value value="50"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-autoeficiente?">
      <value value="false"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="num_robos">
      <value value="2"/>
      <value value="5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="low_batery">
      <value value="30"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_desp">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="robo-eficiente?">
      <value value="false"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="agua">
      <value value="0"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="lixo">
      <value value="20"/>
      <value value="40"/>
      <value value="60"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="Diagonal">
      <value value="true"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="time_charge">
      <value value="1"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="capacidade">
      <value value="50"/>
      <value value="100"/>
    </enumeratedValueSet>
  </experiment>
</experiments>
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 0.0 1.0
0.0 1 1.0 0.0
0.2 0 0.0 1.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180
@#$#@#$#@
0
@#$#@#$#@
