# ══════════════════════════════════════════════════════════
# Solucionador.: Lucio Nunes de Lira
# ══════════════════════════════════════════════════════════
# Competição...: InterFatecs 2023 - 2ª fase
# Programa.....: E - Torres de Hanoi. Again.
# Linguagem....: Python 3
# Interpretador: CPython (3.12.0)
# Versão.......: A (Rev. 0)
# ══════════════════════════════════════════════════════════

def remove_espacos(texto):
    novo = ''
    for caractere in texto:
        if caractere != ' ':
            novo += caractere
    return novo

def main():
    n = int(input())
    discos = remove_espacos(input())
    falta = (2**n-1) - int(discos, 2)
    print(falta)

main()
