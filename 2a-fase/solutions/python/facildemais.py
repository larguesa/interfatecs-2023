# ══════════════════════════════════════════════════════════
# Solucionador.: Lucio Nunes de Lira
# ══════════════════════════════════════════════════════════
# Competição...: InterFatecs 2023 - 2ª fase
# Programa.....: F - facildemais
# Linguagem....: Python 3
# Interpretador: CPython (3.12.0)
# Versão.......: A (Rev. 0)
# ══════════════════════════════════════════════════════════

def main():
    n = int(input())
    impares = 0
    for _ in range(n):
        posicao = int(input())
        if posicao != 1: # apenas o 1º primo é par
            impares += 1
    print('par' if impares % 2 == 0 else 'impar')

main()
