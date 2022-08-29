package com.pineypiney.little_ghost.screens

import glm_.vec2.Vec2

data class MovementMechanics(val gravity: Vec2, val terminalV: Float, val friction: Float, val maxSpeed: Float, val vLims: Vec2) {
}