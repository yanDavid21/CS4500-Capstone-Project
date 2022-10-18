package Players

class Riemann: AbstractOrderingStrategy(compareBy({ it.row.value  }, { it.col.value }))
