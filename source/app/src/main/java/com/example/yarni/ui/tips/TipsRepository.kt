package com.example.yarni.ui.tips

data class Tip(
    val title: String,
    val description: String
)

object TipsRepository {

    val allTips = listOf(
        Tip(
            "Keep Your Tension Steady",
            "Maintaining consistent yarn tension helps your stitches look even and smooth..."
        ),
        Tip(
            "Pick the Right Hook",
            "Using the right hook size makes a huge difference in comfort and stitch quality..."
        ),
        Tip(
            "Don’t Crochet Too Tight",
            "Overly tight stitches are hard to work into and can strain your hands..."
        ),
        Tip(
            "Count as You Go",
            "Losing or adding stitches can distort your project quickly..."
        ),
        Tip(
            "Use a Stitch Marker",
            "Stitch markers help you keep track of the beginning or end of a row..."
        ),
        Tip(
            "Switch Colors Cleanly",
            "For sharp color transitions, always finish the last stitch using the new color..."
        ),
        Tip(
            "Learn to Read Crochet Charts",
            "Symbol charts may look intimidating, but they follow a simple universal logic..."
        ),
        Tip(
            "Manage Your Loose Ends",
            "When using multiple colors or yarns, keep the strands organized at the back..."
        ),
        Tip(
            "Experiment with Different Fibers",
            "Each yarn type behaves differently—cotton gives crisp stitches, while wool adds warmth..."
        ),
        Tip(
            "Block for a Better Finish",
            "Blocking means shaping your project with gentle moisture and pins..."
        ),
        Tip(
            "Play with Texture Stitches",
            "Front and back post stitches add depth, cables, and 3D effects..."
        ),
        Tip(
            "Hide the Spiral in Round Projects",
            "The row jog can be reduced or hidden with special techniques..."
        ),
        Tip(
            "Modify Patterns with Confidence",
            "Adjust sizes, swap stitches, or tweak shaping to suit your vision..."
        ),
        Tip(
            "Explore Tunisian Crochet",
            "Tunisian crochet blends knitting and crochet for a distinct woven texture..."
        ),
        Tip(
            "Break Big Projects into Modules",
            "Working in smaller sections makes big projects easier and keeps stitches consistent..."
        )
    )
}
