package com.example.data

import com.example.model.Album
import com.example.model.Artist
import com.example.model.LyricLine
import com.example.model.Playlist
import com.example.model.Song

object MockMusicCatalog {

    val sampleSongs: List<Song> = listOf(
        Song(
            id = "s1",
            title = "Midnight Neon",
            artist = "Luna Nova",
            album = "Neon Odyssey",
            durationMs = 195000L, // 3:15
            genre = "Synthwave",
            releaseYear = 2025,
            isFavorite = true,
            isDownloaded = true,
            primaryColorHex = 0xFF7B2CBFL, // Purple
            secondaryColorHex = 0xFF00F5D4L, // Cyan
            baseFrequencyHz = 220.0,
            synthPattern = "synthwave",
            lyrics = listOf(
                LyricLine(0L, "[Instrumental Intro - Synth Arpeggios]"),
                LyricLine(8000L, "City lights are glowing through the mist"),
                LyricLine(16000L, "Shadows dance in colors we both missed"),
                LyricLine(24000L, "Driving fast on an endless digital road"),
                LyricLine(32000L, "Leaving behind the stories we were told"),
                LyricLine(42000L, "Midnight neon, guide me through the dark"),
                LyricLine(50000L, "Every heartbeat ignites a glowing spark"),
                LyricLine(58000L, "Take my hand before the morning comes"),
                LyricLine(66000L, "To the rhythm of the cybernetic drums"),
                LyricLine(76000L, "[Bass Drop & Electric Pulse]"),
                LyricLine(92000L, "Reflections in the rearview glass"),
                LyricLine(100000L, "All these memories fade away so fast"),
                LyricLine(110000L, "We are electric in the summer rain"),
                LyricLine(118000L, "Washing away the echoes of the pain"),
                LyricLine(128000L, "Midnight neon, take us far away"),
                LyricLine(136000L, "Where the twilight turns tomorrow to today"),
                LyricLine(148000L, "Endless echoes in the neon sky"),
                LyricLine(160000L, "We will never say goodbye"),
                LyricLine(175000L, "[Outro - Fading Chords & Cosmic Reverb]")
            )
        ),
        Song(
            id = "s2",
            title = "Echoes of Silence",
            artist = "Aura Beats",
            album = "Rainy Nights in Tokyo",
            durationMs = 210000L, // 3:30
            genre = "Lo-Fi Chill",
            releaseYear = 2024,
            isFavorite = true,
            isDownloaded = true,
            primaryColorHex = 0xFF3A86FFL, // Royal Blue
            secondaryColorHex = 0xFFFF006EL, // Neon Pink
            baseFrequencyHz = 196.0,
            synthPattern = "lofi",
            lyrics = listOf(
                LyricLine(0L, "[Raindrops falling on pavement with tape vinyl crackle]"),
                LyricLine(10000L, "Warm coffee by the foggy window sill"),
                LyricLine(22000L, "Time moves slow, everything is still"),
                LyricLine(34000L, "Gentle piano playing in the room"),
                LyricLine(46000L, "Chasing away the quiet afternoon gloom"),
                LyricLine(58000L, "Echoes of silence, speak to my mind"),
                LyricLine(70000L, "Peace is the only thing left to find"),
                LyricLine(85000L, "[Soft acoustic Rhodes solo]"),
                LyricLine(110000L, "Distant sirens hum across the street"),
                LyricLine(124000L, "Gentle tap of a slow steady beat"),
                LyricLine(138000L, "Close your eyes, let the worries fade"),
                LyricLine(152000L, "In this quiet sanctuary we made"),
                LyricLine(170000L, "Echoes of silence, calm and deep"),
                LyricLine(185000L, "Rocking the busy world fast asleep")
            )
        ),
        Song(
            id = "s3",
            title = "Electric Horizon",
            artist = "Solaris",
            album = "Cosmic Resonance",
            durationMs = 180000L, // 3:00
            genre = "Electronic",
            releaseYear = 2026,
            isFavorite = false,
            isDownloaded = false,
            primaryColorHex = 0xFFFF5400L, // Orange
            secondaryColorHex = 0xFFFFD166L, // Gold
            baseFrequencyHz = 261.63,
            synthPattern = "electronic",
            lyrics = listOf(
                LyricLine(0L, "[Fast-paced arpeggiator build-up]"),
                LyricLine(12000L, "Rising up above the atmosphere"),
                LyricLine(24000L, "The stratosphere is crystal clear"),
                LyricLine(36000L, "Electric horizon, burning bright"),
                LyricLine(48000L, "Sailing past the speed of light"),
                LyricLine(60000L, "[Energy Surge & Sub Bass]"),
                LyricLine(80000L, "Gravity cannot hold us down"),
                LyricLine(92000L, "We wear the supernova crown"),
                LyricLine(108000L, "Into the nebula, far and wide"),
                LyricLine(120000L, "Ride the electric stellar tide"),
                LyricLine(140000L, "[Peak Electronic Climax]"),
                LyricLine(160000L, "Drifting smoothly into cosmic space")
            )
        ),
        Song(
            id = "s4",
            title = "Acoustic Rain",
            artist = "Velvet Dusk",
            album = "Unplugged Sessions",
            durationMs = 205000L, // 3:25
            genre = "Acoustic",
            releaseYear = 2023,
            isFavorite = true,
            isDownloaded = false,
            primaryColorHex = 0xFF06D6A0L, // Teal green
            secondaryColorHex = 0xFF118AB2L, // Ocean blue
            baseFrequencyHz = 174.61,
            synthPattern = "acoustic",
            lyrics = listOf(
                LyricLine(0L, "[Fingerstyle acoustic strings]"),
                LyricLine(14000L, "Waking up to the gray skies above"),
                LyricLine(26000L, "Remembering the simple warmth of love"),
                LyricLine(38000L, "Gentle strings whisper in the breeze"),
                LyricLine(50000L, "Rustling softly through the willow trees"),
                LyricLine(64000L, "Acoustic rain, washing yesterday clean"),
                LyricLine(78000L, "The prettiest morning I have ever seen"),
                LyricLine(94000L, "[Harmonica & Fingerpicked Interlude]"),
                LyricLine(115000L, "Step outside, breathe the fresh cool air"),
                LyricLine(130000L, "Not a single sorrow or a worry to bear"),
                LyricLine(145000L, "Acoustic rain, sing your melody"),
                LyricLine(165000L, "Setting every tired spirit free")
            )
        ),
        Song(
            id = "s5",
            title = "Cyber Drift",
            artist = "Kavinsky Wave",
            album = "Neon Odyssey",
            durationMs = 225000L, // 3:45
            genre = "Synthwave",
            releaseYear = 2025,
            isFavorite = false,
            isDownloaded = true,
            primaryColorHex = 0xFFE63946L, // Crimson
            secondaryColorHex = 0xFF457B9DL, // Slate Blue
            baseFrequencyHz = 246.94,
            synthPattern = "cyber",
            lyrics = listOf(
                LyricLine(0L, "[Engine rev & Analog Bassline]"),
                LyricLine(15000L, "Tires screeching through the midnight grid"),
                LyricLine(30000L, "Doing all the things that they forbid"),
                LyricLine(45000L, "Cyber drift, smoking on the track"),
                LyricLine(60000L, "Once you hit nitro, there is no looking back"),
                LyricLine(80000L, "[High-Octane Synth Solo]"),
                LyricLine(110000L, "Speeding through the digital neon haze"),
                LyricLine(130000L, "Lost inside this computerized maze"),
                LyricLine(155000L, "Hit the gas, feel the rush ignite"),
                LyricLine(180000L, "King of the streets under neon light")
            )
        ),
        Song(
            id = "s6",
            title = "Starlight Lullaby",
            artist = "The Midnight Echo",
            album = "Cosmic Resonance",
            durationMs = 175000L, // 2:55
            genre = "Dream Pop",
            releaseYear = 2026,
            isFavorite = true,
            isDownloaded = false,
            primaryColorHex = 0xFF8338ECL, // Deep Purple
            secondaryColorHex = 0xFFFF006EL, // Neon Pink
            baseFrequencyHz = 293.66,
            synthPattern = "dream",
            lyrics = listOf(
                LyricLine(0L, "[Ethereal bell tones and soft pads]"),
                LyricLine(12000L, "Stars are twinkling high above the bay"),
                LyricLine(24000L, "As the golden twilight slips away"),
                LyricLine(38000L, "Close your eyes, sleep until tomorrow"),
                LyricLine(52000L, "Free from every burden and from sorrow"),
                LyricLine(68000L, "Starlight lullaby, singing soft and slow"),
                LyricLine(82000L, "Watch the milky way softly glow"),
                LyricLine(100000L, "[Dreamy Celestial Arpeggio]"),
                LyricLine(125000L, "Rest your head upon the velvet night"),
                LyricLine(145000L, "Everything is going to be alright")
            )
        ),
        Song(
            id = "s7",
            title = "Urban Rhythm",
            artist = "Aura Beats",
            album = "After Hours",
            durationMs = 190000L, // 3:10
            genre = "Hip-Hop",
            releaseYear = 2024,
            isFavorite = false,
            isDownloaded = true,
            primaryColorHex = 0xFFFB8500L, // Amber
            secondaryColorHex = 0xFF023047L, // Midnight Navy
            baseFrequencyHz = 164.81,
            synthPattern = "urban",
            lyrics = listOf(
                LyricLine(0L, "[Boom-bap drum pattern with vinyl scratch]"),
                LyricLine(10000L, "Concrete jungle underneath our feet"),
                LyricLine(22000L, "Living to the tempo of the urban beat"),
                LyricLine(35000L, "Graffiti murals painted on the brick"),
                LyricLine(48000L, "Clocks are running, hear them tick"),
                LyricLine(62000L, "City flow, moving through the crowd"),
                LyricLine(75000L, "Play this record and play it loud"),
                LyricLine(95000L, "[Sub bass groove & trumpet chop]"),
                LyricLine(120000L, "From downtown avenue to uptown drive"),
                LyricLine(138000L, "This is the city that keeps us alive")
            )
        ),
        Song(
            id = "s8",
            title = "Solitude Piano",
            artist = "Velvet Dusk",
            album = "Unplugged Sessions",
            durationMs = 240000L, // 4:00
            genre = "Classical",
            releaseYear = 2023,
            isFavorite = false,
            isDownloaded = false,
            primaryColorHex = 0xFF4A4E69L, // Slate
            secondaryColorHex = 0xFF22223BL, // Dark obsidian
            baseFrequencyHz = 220.0,
            synthPattern = "piano",
            lyrics = listOf(
                LyricLine(0L, "[Grand piano minor progression]"),
                LyricLine(20000L, "Keys softly pressed in the silent hall"),
                LyricLine(42000L, "Shadows lengthen against the wall"),
                LyricLine(65000L, "Notes that evoke a story untold"),
                LyricLine(90000L, "A heart of music that never grows old"),
                LyricLine(120000L, "[Crescendo & Harmonic Cadence]"),
                LyricLine(150000L, "Let the resonance linger and breathe"),
                LyricLine(180000L, "A timeless tapestry of sound we weave")
            )
        )
    )

    val samplePlaylists: List<Playlist> = listOf(
        Playlist(
            id = "p1",
            name = "Late Night Drive",
            description = "Neon synths and atmospheric beats for empty highways under starlight.",
            songIds = listOf("s1", "s5", "s3"),
            isCustom = false,
            coverGradientStart = 0xFF7B2CBFL,
            coverGradientEnd = 0xFF00F5D4L
        ),
        Playlist(
            id = "p2",
            name = "Deep Focus & Study",
            description = "Lo-fi textures and ambient rain chords to boost deep productivity.",
            songIds = listOf("s2", "s4", "s8"),
            isCustom = false,
            coverGradientStart = 0xFF3A86FFL,
            coverGradientEnd = 0xFF06D6A0L
        ),
        Playlist(
            id = "p3",
            name = "Cardio & Workout Surge",
            description = "High tempo electronic bangers to push past your personal limits.",
            songIds = listOf("s3", "s5", "s7"),
            isCustom = false,
            coverGradientStart = 0xFFFF5400L,
            coverGradientEnd = 0xFFE63946L
        ),
        Playlist(
            id = "p4",
            name = "Calm Sleep & Meditation",
            description = "Peaceful lullabies and harmonic soundscapes to drift away into sleep.",
            songIds = listOf("s6", "s2", "s8"),
            isCustom = false,
            coverGradientStart = 0xFF8338ECL,
            coverGradientEnd = 0xFF3A86FFL
        )
    )

    val sampleArtists: List<Artist> = listOf(
        Artist(
            id = "a1",
            name = "Luna Nova",
            bio = "Pioneering synthwave producer and vocalist blending 80s analog nostalgia with modern cyberpunk soundscapes.",
            monthlyListeners = "1,420,800",
            primaryColorHex = 0xFF7B2CBFL,
            topSongs = listOf(sampleSongs[0])
        ),
        Artist(
            id = "a2",
            name = "Aura Beats",
            bio = "Tokyo-based beatmaker renowned for mellow tape-distorted lo-fi tracks and soothing jazz chords.",
            monthlyListeners = "2,150,000",
            primaryColorHex = 0xFF3A86FFL,
            topSongs = listOf(sampleSongs[1], sampleSongs[6])
        ),
        Artist(
            id = "a3",
            name = "Solaris",
            bio = "Interstellar electronic composer creating soaring progressive melodies and festival anthems.",
            monthlyListeners = "980,400",
            primaryColorHex = 0xFFFF5400L,
            topSongs = listOf(sampleSongs[2])
        ),
        Artist(
            id = "a4",
            name = "Velvet Dusk",
            bio = "Multi-instrumentalist duo crafting heartwarming acoustic ballads and neoclassical piano masterworks.",
            monthlyListeners = "740,200",
            primaryColorHex = 0xFF06D6A0L,
            topSongs = listOf(sampleSongs[3], sampleSongs[7])
        ),
        Artist(
            id = "a5",
            name = "Kavinsky Wave",
            bio = "Dark synth maestro known for aggressive basslines, highway anthems, and cinematic retro electro.",
            monthlyListeners = "1,890,300",
            primaryColorHex = 0xFFE63946L,
            topSongs = listOf(sampleSongs[4])
        ),
        Artist(
            id = "a6",
            name = "The Midnight Echo",
            bio = "Indie dream-pop project floating on ethereal guitars, reverb-soaked vocals, and ambient bells.",
            monthlyListeners = "620,100",
            primaryColorHex = 0xFF8338ECL,
            topSongs = listOf(sampleSongs[5])
        )
    )

    val sampleAlbums: List<Album> = listOf(
        Album(
            id = "al1",
            title = "Neon Odyssey",
            artist = "Luna Nova",
            year = 2025,
            primaryColorHex = 0xFF7B2CBFL,
            secondaryColorHex = 0xFF00F5D4L,
            songs = listOf(sampleSongs[0], sampleSongs[4])
        ),
        Album(
            id = "al2",
            title = "Rainy Nights in Tokyo",
            artist = "Aura Beats",
            year = 2024,
            primaryColorHex = 0xFF3A86FFL,
            secondaryColorHex = 0xFFFF006EL,
            songs = listOf(sampleSongs[1])
        ),
        Album(
            id = "al3",
            title = "Cosmic Resonance",
            artist = "Solaris",
            year = 2026,
            primaryColorHex = 0xFFFF5400L,
            secondaryColorHex = 0xFFFFD166L,
            songs = listOf(sampleSongs[2], sampleSongs[5])
        ),
        Album(
            id = "al4",
            title = "Unplugged Sessions",
            artist = "Velvet Dusk",
            year = 2023,
            primaryColorHex = 0xFF06D6A0L,
            secondaryColorHex = 0xFF118AB2L,
            songs = listOf(sampleSongs[3], sampleSongs[7])
        ),
        Album(
            id = "al5",
            title = "After Hours",
            artist = "Aura Beats",
            year = 2024,
            primaryColorHex = 0xFFFB8500L,
            secondaryColorHex = 0xFF023047L,
            songs = listOf(sampleSongs[6])
        )
    )
}
