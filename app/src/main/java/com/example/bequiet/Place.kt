package com.example.bequiet

class Place {
    var id = 0
    var name: String
    var volume: Int = 0
    var x: Double = 0.0
    var y: Double = 0.0
    var range: Int = 0

    internal constructor(name: String, volume: Int, x: Double, y: Double, range: Int) {
        this.name = name
        this.volume = volume
        this.x = x
        this.y = y
        this.range = range
    }
    internal constructor(id: Int, name: String, volume: Int, x: Double, y: Double, range: Int) {
        this.id = id
        this.name = name
        this.volume = volume
        this.x = x
        this.y = y
        this.range = range
    }
}