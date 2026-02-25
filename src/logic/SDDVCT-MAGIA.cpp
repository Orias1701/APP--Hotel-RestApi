class sddvct {
	String masddv;
	int soluong;
	float tongtien; 
}; 

class sddv {
	String masddv;
};

class dv {
	String madv;
	float gia;
};

int main() {
	float giadv;
	for(i : sddvct) {
		if(sddvct.madv == dv.madv) {
			giadv = dv.gia;
		}
	}
	sddvct.tongtien = giadv * sddvct.soluong;
	printf(sddvct.tongtien);
}

