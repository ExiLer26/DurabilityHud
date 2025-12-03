# Durability HUD - Minecraft Fabric 1.20.1 Modu

Bu mod, Minecraft 1.20.1 için hazırlanmış bir Fabric modudur. Elinizde ve üzerinizde bulunan itemlerin dayanıklılığını (durability) HUD olarak gösterir.

## Özellikler

- **Item Icon ve Durability Gösterme**: Tüm dayanıklılığı olan itemlerin icon ve durability değerini gösterir
- **Özelleştirilebilir Konum**: HUD'ın X ve Y pozisyonunu ayarlayabilirsiniz
- **Boyut Ayarlama**: HUD boyutunu 0.1x ile 5.0x arasında ayarlayabilirsiniz
- **Item Bazlı Açma/Kapama**: Her item türünü (kılıç, kazma, zırh parçaları vb.) ayrı ayrı gösterebilir veya gizleyebilirsiniz
- **Y Tuşu ile Ayar Menüsü**: Oyun içinde Y tuşuna basarak ayarları değiştirebilirsiniz
- **Renk Kodlaması**: Durability değerine göre yeşil, sarı, kırmızı renk gösterir
- **Sabitlenmiş Bloklar**: Belirli blokların envanterinizdeki sayısını takip edin

## Desteklenen Itemler

- Kılıç, Kazma, Balta, Kürek, Çapa
- Miğfer, Göğüslük, Pantolon, Çizme
- Kalkan, Elytra
- Yay, Tatar Yayı, Üç Dişli Mızrak
- Olta, Makas

## Kurulum (Minecraft İçin)

### Gereksinimler
- Minecraft Java Edition 1.20.1
- Fabric Loader 0.15.3 veya daha yenisi
- Fabric API 0.91.0+
- Java 17 veya üzeri

### Adımlar

1. **Fabric Kurulumu**:
   - [Fabric İndirme Sayfası](https://fabricmc.net/use/installer/) adresinden Fabric Installer'ı indirin
   - Installer'ı çalıştırın ve Minecraft 1.20.1'i seçin

2. **Fabric API'yi İndirin**:
   - [Fabric API](https://modrinth.com/mod/fabric-api) sayfasından 1.20.1 versiyonunu indirin
   - JAR dosyasını `.minecraft/mods/` klasörüne kopyalayın

3. **Modu Derleyin**:
   ```bash
   ./gradlew build
   ```
   
4. **JAR Dosyasını Alın**:
   - Build işlemi tamamlandığında `build/libs/` klasöründe `durabilityhud-1.0.0.jar` dosyasını bulacaksınız

5. **Modu Yükleyin**:
   - JAR dosyasını `.minecraft/mods/` klasörüne kopyalayın
   - Windows: `%APPDATA%\.minecraft\mods\`
   - macOS: `~/Library/Application Support/minecraft/mods/`
   - Linux: `~/.minecraft/mods/`

6. **Minecraft'ı Başlatın**:
   - Minecraft Launcher'ı açın
   - Fabric 1.20.1 profilini seçin
   - Oyunu başlatın

## Kullanım

### Ayar Menüsünü Açma
Oyun içindeyken **Y** tuşuna basın.

### Ayarlar

**Genel Ayarlar**:
- **HUD Aktif**: Modun tamamını açma/kapama
- **X Pozisyon**: HUD'ın ekranın solundan uzaklığı (piksel)
- **Y Pozisyon**: HUD'ın ekranın üstünden uzaklığı (piksel)
- **Boyut**: HUD'ın ölçeği (0.1 - 5.0)

**Sabit Bloklar**:
- Belirli blokları sabitleyerek envanterinizdeki sayılarını takip edin
- Elde tutulan bir bloğu sabitleyebilirsiniz

**Item Ayarları**:
Her item türü için ayrı ayrı göster/gizle seçeneği bulunur.

### Config Dosyası

Mod ayarları `config/durabilityhud.json` dosyasında saklanır. Bu dosyayı manuel olarak da düzenleyebilirsiniz.

## Geliştirme

### Gereksinimler
- Java Development Kit (JDK) 17 veya üzeri
- IntelliJ IDEA (önerilen) veya Eclipse

### Projeyi Derleme

```bash
./gradlew build
```

### Proje Yapısı

```
src/
├── client/java/com/durabilityhud/
│   ├── client/
│   │   ├── DurabilityHudClient.java  # Ana mod giriş noktası
│   │   ├── HudRenderer.java          # HUD çizim işlemleri
│   │   ├── KeyBindings.java          # Tuş atamaları
│   │   └── ConfigScreen.java         # Ayar ekranı
│   └── config/
│       └── ModConfig.java            # Yapılandırma sistemi
└── main/resources/
    ├── fabric.mod.json               # Mod metadata
    └── assets/durabilityhud/lang/    # Dil dosyaları
```

## Lisans

MIT License - Projeyi serbestçe kullanabilir, değiştirebilir ve dağıtabilirsiniz.

## Destek

Herhangi bir sorun veya öneri için:
1. Önce `config/durabilityhud.json` dosyasını kontrol edin
2. Minecraft loglarını kontrol edin (`logs/latest.log`)
3. Fabric Loader ve Fabric API versiyonlarınızın güncel olduğundan emin olun

## Changelog

### v1.0.0
- Fabric 1.20.1'e dönüştürüldü
- Item icon ve durability HUD
- Özelleştirilebilir konum ve boyut
- Item bazlı göster/gizle
- Sabitlenmiş blok takibi
- Y tuşu ile ayar menüsü
