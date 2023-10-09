# ══════════════════════════════════════════════════════════
# Solucionador.: Lucio Nunes de Lira
# ══════════════════════════════════════════════════════════
# Competição...: InterFatecs 2023 - 2ª fase
# Programa.....: B - Impressora 3D Unidimensional.
# Linguagem....: Python 3
# Interpretador: CPython (3.12.0)
# Versão.......: A (Rev. 0)
# ══════════════════════════════════════════════════════════

def main():
    largura, altura, qtd_comandos = [int(x) for x in input().split()]
    impressao = (largura+1) * [0]
    for i in range(qtd_comandos):
        inicio, fim, qtd_material = [int(x) for x in input().split()]
        for j in range(inicio, fim+1):
            impressao[j] += qtd_material
    altura_maxima = max(impressao)
    print(altura_maxima if altura_maxima < altura else 'invalida')

main()
