# ══════════════════════════════════════════════════════════
# Solucionador.: Lucio Nunes de Lira
# ══════════════════════════════════════════════════════════
# Competição...: InterFatecs 2023 - 2ª fase
# Programa.....: J - Pim
# Linguagem....: Python 3
# Interpretador: CPython (3.12.0)
# Versão.......: A (Rev. 0)
# ══════════════════════════════════════════════════════════

n = int(input())

print(1, end='')
for i in range(2, n+1):
    print(f' {"pim" if i % 4 == 0 else i}', end='')
print()
