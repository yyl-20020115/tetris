package com.noc.tet.pieces;

import com.noc.tet.Square;

import android.content.Context;

public class LPiece extends Piece3x3 {

	private final Square lSquare;
	
	public LPiece(Context c) {
		super(c);
		lSquare = new Square(Piece.type_L,c);
		pattern[1][0] = lSquare;
		pattern[1][1] = lSquare;
		pattern[1][2] = lSquare;
		pattern[2][0] = lSquare;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][0] = lSquare;
		pattern[1][1] = lSquare;
		pattern[1][2] = lSquare;
		pattern[2][0] = lSquare;
		reDraw();
	}

}
