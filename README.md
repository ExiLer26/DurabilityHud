# Durability HUD - Minecraft Forge 1.20.1 Modu

Bu mod, Minecraft 1.20.1 için hazırlanmış bir Forge modudur. Elinizde ve üzerinizde bulunan itemlerin dayanıklılığını (durability) HUD olarak gösterir.

## Özellikler

- ✅ **Item Icon ve Durability Gösterme**: Tüm dayanıklılığı olan itemlerin icon ve durability değerini gösterir
- ✅ **Özelleştirilebilir Konum**: HUD'ın X ve Y pozisyonunu ayarlayabilirsiniz
- ✅ **Boyut Ayarlama**: HUD boyutunu 0.1x ile 5.0x arasında ayarlayabilirsiniz
- ✅ **Item Bazlı Açma/Kapama**: Her item türünü (kılıç, kazma, zırh parçaları vb.) ayrı ayrı gösterebilir veya gizleyebilirsiniz
- ✅ **Y Tuşu ile Ayar Menüsü**: Oyun içinde Y tuşuna basarak ayarları değiştirebilirsiniz
- ✅ **Renk Kodlaması**: Durability değerine göre yeşil, sarı, kırmızı renk gösterir

## Desteklenen İtemler

- Kılıç, Kazma, Balta, Kürek, Çapa
- Miğfer, Göğüslük, Pantolon, Çizme
- Kalkan, Elytra
- Yay, Tatar Yayı, Üç Dişli Mızrak
- Olta, Makas

## Kurulum (Minecraft İçin)

### Gereksinimler
- Minecraft Java Edition 1.20.1
- Minecraft Forge 47.3.0 veya daha yenisi
- Java 17 veya üzeri

### Adımlar

1. **Forge Kurulumu**:
   - [Forge İndirme Sayfası](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html) adresinden Forge 1.20.1'i indirin
   - Installer'ı çalıştırın ve "Install client" seçeneğini seçin

2. **Modu Derleyin**:
   ```bash
   ./gradlew build
   ```
   
3. **JAR Dosyasını Alın**:
   - Build işlemi tamamlandığında `build/libs/` klasöründe `DurabilityHud-1.0.0.jar` dosyasını bulacaksınız

4. **Modu Yükleyin**:
   - JAR dosyasını `.minecraft/mods/` klasörüne kopyalayın
   - Windows: `%APPDATA%\.minecraft\mods\`
   - macOS: `~/Library/Application Support/minecraft/mods/`
   - Linux: `~/.minecraft/mods/`

5. **Minecraft'ı Başlatın**:
   - Minecraft Launcher'ı açın
   - Forge 1.20.1 profilini seçin
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

**İtem Ayarları**:
Her item türü için ayrı ayrı göster/gizle seçeneği bulunur.

### Config Dosyası

Mod ayarları `config/durabilityhud-client.toml` dosyasında saklanır. Bu dosyayı manuel olarak da düzenleyebilirsiniz.

## Geliştirme

### Gereksinimler
- Java Development Kit (JDK) 17 veya üzeri
- IntelliJ IDEA (önerilen) veya Eclipse

### Projeyi Açma

1. **Gradle Build**:
   ```bash
   ./gradlew build
   ```

2. **IntelliJ IDEA için**:
   ```bash
   ./gradlew genIntellijRuns
   ```
   Sonra IntelliJ IDEA'da "Open" ile projeyi açın.

3. **Eclipse için**:
   ```bash
   ./gradlew genEclipseRuns
   ```
   Sonra Eclipse'de "Import Existing Gradle Project" yapın.

### Oyunu Çalıştırma

IntelliJ IDEA veya Eclipse'de:
- **runClient**: Minecraft client'ı mod ile başlatır
- **runServer**: Minecraft server'ı mod ile başlatır

### Proje Yapısı

```
src/main/java/com/durabilityhud/
├── DurabilityHudMod.java          # Ana mod sınıfı
├── config/
│   └── ModConfig.java             # Konfigürasyon yönetimi
└── client/
    ├── HudRenderer.java           # HUD render mantığı
    ├── KeyBindings.java           # Tuş tanımlamaları
    └── ConfigScreen.java          # Ayar ekranı GUI
```

## Lisans

MIT License - Projeyi serbestçe kullanabilir, değiştirebilir ve dağıtabilirsiniz.

## Destek

Herhangi bir sorun veya öneri için:
1. Önce `config/durabilityhud-client.toml` dosyasını kontrol edin
2. Minecraft loglarını kontrol edin (`logs/latest.log`)
3. Forge versiyonunuzun 47.3.0 veya üzeri olduğundan emin olun

## Changelog

### v1.0.0
- İlk sürüm
- Item icon ve durability HUD
- Özelleştirilebilir konum ve boyut
- Item bazlı göster/gizle
- Y tuşu ile ayar menüsü
