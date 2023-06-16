import requests
from io import StringIO
from ortools.linear_solver import pywraplp

def solve_warehouse_location(depo_sayisi, musteri_sayisi, depo_kapasiteleri, depo_maliyetleri, musteri_talepleri, musteri_depo_maliyetleri):
    solver = pywraplp.Solver.CreateSolver('SCIP')

    # Depo seçimi değişkenleri
    x = []
    for i in range(depo_sayisi):
        x.append([])
        for j in range(musteri_sayisi):
            x[i].append(solver.IntVar(0, 1, f'x{i}_{j}'))

    # Problemin kısıtları

    # Her müşteri sadece bir depoya atanır
    for j in range(musteri_sayisi):
        solver.Add(sum(x[i][j] for i in range(depo_sayisi)) == 1)

    # Depo kapasite kısıtları
    for i in range(depo_sayisi):
        solver.Add(sum(x[i][j] * musteri_talepleri[j] for j in range(musteri_sayisi)) <= depo_kapasiteleri[i])

    # Toplam kurulum maliyetini hesaplama
    objective = solver.Objective()
    for i in range(depo_sayisi):
        for j in range(musteri_sayisi):
            objective.SetCoefficient(x[i][j], musteri_depo_maliyetleri[j][i])
    objective.SetMinimization()

    # En optimal çözümü bul
    status = solver.Solve()

    if status == pywraplp.Solver.OPTIMAL:
        print('Optimal maliyet:', objective.Value())

        # Müşterileri depolara atanma sırasına göre düzenleme
        atanma_sirasi = []
        for j in range(musteri_sayisi):
            for i in range(depo_sayisi):
                if x[i][j].solution_value() == 1:
                    atanma_sirasi.append((j, i))
                    break

        # Atama sırasına göre müşterileri yazdırma
        atanma_sirasi.sort()
        for j, i in atanma_sirasi:
            maliyet = musteri_depo_maliyetleri[j][i]
            print(f"{i}", end=" ") # Müşteri {j} -> Depo {i} (Maliyet: {maliyet})
    else:
        print('Optimal çözüm bulunamadi.')

def read_data_from_url(url):
    response = requests.get(url)
    data = response.content.decode('utf-8')
    file = StringIO(data)
    lines = file.readlines()

    depo_sayisi = int(lines[0].split()[0])
    musteri_sayisi = int(lines[0].split()[1])

    depo_kapasiteleri = []
    depo_maliyetleri = []
    musteri_talepleri = []
    musteri_depo_maliyetleri = []

    for i in range(1, depo_sayisi + 1):
        depo_verileri = lines[i].split()
        depo_kapasitesi = int(depo_verileri[0])
        depo_maliyeti = float(depo_verileri[1])

        depo_kapasiteleri.append(depo_kapasitesi)
        depo_maliyetleri.append(depo_maliyeti)

    musteri_talepleri_verisi = lines[depo_sayisi + 1:]
    musteri_talepleri_verisi = [line.strip() for line in musteri_talepleri_verisi if line.strip() != '']

    for i in range(0, len(musteri_talepleri_verisi), 2):
        talep = int(musteri_talepleri_verisi[i])
        musteri_talepleri.append(talep)

        depo_maliyeti = [float(x) for x in musteri_talepleri_verisi[i + 1].split()]
        musteri_depo_maliyetleri.append(depo_maliyeti)

    return depo_sayisi, musteri_sayisi, depo_kapasiteleri, depo_maliyetleri, musteri_talepleri, musteri_depo_maliyetleri

# Veri dosyasının URL'si
url_16 = 'https://raw.githubusercontent.com/SamedTemiz/WarehouseLocationProblem/main/dataset/wl_16_1'
url_200 = 'https://raw.githubusercontent.com/SamedTemiz/WarehouseLocationProblem/main/dataset/wl_200_2'
url_500 = 'https://raw.githubusercontent.com/SamedTemiz/WarehouseLocationProblem/main/dataset/wl_500_3'

# Verileri URL'den okuyarak çözüm metoduyla çalıştırma
depo_sayisi, musteri_sayisi, depo_kapasiteleri, depo_maliyetleri, musteri_talepleri, musteri_depo_maliyetleri = read_data_from_url(url_16) # Buradan diğer veri setine geçilebilir

# Çözüm
solve_warehouse_location(depo_sayisi, musteri_sayisi, depo_kapasiteleri, depo_maliyetleri, musteri_talepleri, musteri_depo_maliyetleri)
