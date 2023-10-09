# ══════════════════════════════════════════════════════════
# Solucionador.: Lucio Nunes de Lira
# ══════════════════════════════════════════════════════════
# Competição...: InterFatecs 2023 - 2ª fase
# Programa.....: I - Criptografia hash
# Linguagem....: Python 3
# Interpretador: CPython (3.12.0)
# Versão.......: A (Rev. 0)
# ══════════════════════════════════════════════════════════

from math import ceil, sqrt

def coleta_entrada():
    clientes = []
    entrada = input()
    while entrada != 'ACABOU':
        usuario, senha = entrada.split()
        clientes.append([usuario, senha])
        entrada = input()
    return clientes

def primo(n):
    if n % 2 == 0: return n == 2
    raiz = ceil(sqrt(n))
    for d in range(3, raiz+1, 2):
        if n % d == 0:
            return False
    return True

def lista_primos(limite):
    if limite < 2: return []
    primos = [2]
    for n in range(3, limite+1, 2):
        if primo(n):
            primos.append(n)
    return primos

def decompoe(n, primos=[]):
    fatores = []
    for primo in primos:
        while n % primo == 0:
            fatores.append(primo)
            n = n // primo
        if n == 1: break
    return fatores

def transforma_ascii(texto):
    n = 0
    for posicao, caractere in enumerate(texto, 1):
        n += posicao * ord(caractere)
    return n

def exibe(clientes):
    for usuario, valor_hash in clientes:
        print(f'usuario...: {usuario}')
        print(f'valor hash: {valor_hash}')
        print('-' * 30)

def main():
    clientes = coleta_entrada()

    senhas_ascii = []
    for usuario, senha in clientes:
        senhas_ascii.append(transforma_ascii(senha))

    primos = lista_primos(max(senhas_ascii))

    for i, senha_ascii in enumerate(senhas_ascii):
        fatores_primos = decompoe(senha_ascii, primos)
        fatores_texto = ''.join([str(fator) for fator in fatores_primos])
        valor_hash = str(senha_ascii) + fatores_texto
        clientes[i][1] = valor_hash

    clientes.sort(key=lambda item: item[0])
    exibe(clientes)

main()
