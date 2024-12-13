package com.example.solar_system_scope_app.model

class PlanetDataSource() {
    var planets = mutableListOf<PlanetInfo>()
    lateinit var sun812: PlanetInfo
    lateinit var earth812: PlanetInfo
    lateinit var moon812: PlanetInfo
    lateinit var mecury812 : PlanetInfo
    lateinit var saturn812 : PlanetInfo
    lateinit var mars812 : PlanetInfo
    lateinit var jupiter812 : PlanetInfo
    lateinit var uranus812 : PlanetInfo
    lateinit var neptune812 : PlanetInfo
    lateinit var venus812 : PlanetInfo
    suspend fun loadPlanetInfos() : MutableList<PlanetInfo>? {
        val sunBuffer = DataManager.getPlanetBuffer(  "Sun.glb")
        val mercuryBuffer = DataManager.getPlanetBuffer( "Mercury.glb")
        val venusBuffer = DataManager.getPlanetBuffer( "Venus.glb")
        val earthBuffer = DataManager.getPlanetBuffer( "Earth.glb")
        val moonBuffer = DataManager.getPlanetBuffer( "Moon.glb")
        val marsBuffer = DataManager.getPlanetBuffer( "Mars.glb")
        val jupiterBuffer = DataManager.getPlanetBuffer( "Jupiter.glb")
        val saturnBuffer = DataManager.getPlanetBuffer( "Saturn.glb")
        val uranusBuffer = DataManager.getPlanetBuffer( "Uranus.glb")
        val neptuneBuffer = DataManager.getPlanetBuffer( "Neptune.glb")
        val planet :PlanetInfo

            sun812 =  PlanetInfo(
                fileName = "Sun.glb",
                name = "Sun",
                orbitRadiusA = 2f*0f,
                eccentricity = 0f,
                orbitSpeed = .1f*0f,
                scale = 1.5f*0.1f,
                inclination = 0f,
                axisTilt = 0.0f,
                rotation = 0.0f,
                rotationSpeed = (1/86400f)*0.4f,
                buffer = sunBuffer!!,
            )
        mecury812 =     PlanetInfo(
                fileName = "Mercury.glb",
                name = "Mercury",
                orbitRadiusA = 2f*2.0f,
                eccentricity = 0.2056f,
                orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.5f,
                scale = 1.2f*0.05f,

                inclination = 7.0f,
                axisTilt = 0.0f,
                rotation = 0.0f,
                rotationSpeed = (1/86400f)*.1f*1.0f,
                buffer = mercuryBuffer!!
            )

        venus812 =  PlanetInfo(
                fileName = "Venus.glb",
                name = "Venus",
                orbitRadiusA = 2f*3.7f,
                eccentricity = 0.0067f,
                orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.35f,
                scale = 1.2f*0.005f,
                inclination = 3.39f,
                axisTilt = 177.4f,
                rotation = 177.4f ,
                rotationSpeed = (1/86400f)*.1f*-1.48f,
                buffer = venusBuffer!!
            )
        earth812 =   PlanetInfo(
                fileName = "Earth.glb",
                name = "Earth",
                orbitRadiusA = 2f*5.0f,
                eccentricity = 0.0167f,
                orbitSpeed =(1/86400f)*100f*(1/365.25f)*.1f*0.3f,
                scale = 1.2f*0.00525f,
                inclination = 0.00005f,
                axisTilt = 23.44f,
                rotation = 23.44f,
                rotationSpeed = (1/86400f)*2.8f,
                buffer = earthBuffer!!
            )
        moon812 =  PlanetInfo(
                fileName = "Moon.glb",
                name = "Moon",
                orbitRadiusA = 130.9f, // khong duoc thay doi neu doi phai doi cung voi scale trai dat
                eccentricity = 0.0549f,
                orbitSpeed = (1/86400f)*.5f*2.5f,
                scale = 1.2f*12f,// khong duoc thay doi
                inclination = 5.14f,
                axisTilt = 6.68f,
                rotation = 6.68f,
                rotationSpeed = (1/86400f)*.6f*13.36f,
                parentName = earth812,
                buffer = moonBuffer!!
            )
        mars812 =  PlanetInfo(
                fileName = "Mars.glb",
                name = "Mars",
                orbitRadiusA = 2f*7.6f,
                eccentricity = 0.0934f,
                orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.33f,
                scale = 1.2f*0.371f,
                inclination = 1.85f,
                axisTilt = 25.19f,
                rotation = 25.19f,
                rotationSpeed = (1/86400f)*.1f*1.02f,
                buffer = marsBuffer!!
            )
        jupiter812 =   PlanetInfo(
                fileName = "Jupiter.glb",
                name = "Jupiter",
                orbitRadiusA = 2f*11f,
                eccentricity = 0.049f,
                orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.084f,
                scale = 1.2f*0.1f,
                inclination = 1.31f,
                axisTilt = -7.13f,
                rotation = 13.13f,
                rotationSpeed = (1/86400f)*.1f*1.41f,
                buffer = jupiterBuffer!!
            )
        saturn812 =    PlanetInfo(
                fileName = "Saturn.glb",
                name = "Saturn",
                orbitRadiusA = 2f*16f,
                eccentricity = 0.056f,
                orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.01f*2.5f * 0.034f,
                scale = 1.2f*3f,
                inclination = 2.49f,
                axisTilt = 0.0f*23.73f,
                rotation = 23.73f,
                rotationSpeed = (1/86400f)*0.001f*.1f*0.1f*2.24f,
                buffer = saturnBuffer!!
            )
        uranus812 = PlanetInfo(
                fileName = "Uranus.glb",
                name = "Uranus",
                orbitRadiusA = 2f*19.22f,
                eccentricity = 0.046f,
                orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.012f,
                scale = 1.2f*0.001f,
                inclination = 0.77f,
                axisTilt = 0.0f*17.77f,
                rotation = 17.77f,
                rotationSpeed = (1/86400f)*0.001f*.1f*0.41f,
                buffer = uranusBuffer!!
            )
        neptune812 =  PlanetInfo(
                fileName = "Neptune.glb",
                name = "Neptune",
                orbitRadiusA = 2f*22f,
                eccentricity = 0.010f,
                orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.006f,
                scale = 1.2f*0.007f,
                inclination = 1.77f,
                axisTilt = 0.0f*28.32f,
                rotation = 0.0f*28.32f,
                rotationSpeed = (1/86400f)*0.001f*.1f*0.48f,
                buffer = neptuneBuffer!!
            )
         planets = mutableListOf(sun812, earth812 , moon812, mecury812,
             neptune812,
             uranus812,
             saturn812,
             jupiter812,
             mars812,
             venus812)

        return planets
    }
}