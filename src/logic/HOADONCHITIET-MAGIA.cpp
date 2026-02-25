class hoadon{
	string mahd;
	int tongtien; 
}; 
class hoadonct{
	string mahd,maphong,masddv,makt,madp;
	int tongtien; 
}; 
class datphong{
	string madp,maphong;
	int tienphat,ngayhen,ngaynhan,ngaytra; 
}; 
class phong{
	string maphong,maloai; 
}; 
class loaiphong{
	string maloai;
	int gialoai; 
}; 
class ktphong{
	string makt;
	int tienden; 
}; 
class sddv{
	string masddv;
	int tongtien; 
}; 
void int giaphong(int giaphong){
	giaphong=(datphong.maphong->phong.maphong,phong.maloai->loaiphong.gialoai)
	return giaphong; 
} 
void int tienphong(int tienphong){
	tienphong=giaphong*(datphong.ngayhen-datphong.ngaynhan)+datphong.tienphat; 
	return tienphong; 
} 
int main(){
	hoadonct.tongtien=tienphong+ktphong.tienden+sddv.tongtien;
	print(hoadonct.tongtien) ;
	if(hoadonct.mahd=hoadon.mahd){
		hoadon.tongtien=sum(hoadonct.tongtien);
	}
	print(hoadon.tongtien); 
} 
