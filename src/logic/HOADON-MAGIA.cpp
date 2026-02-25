class hdct{
	string mahd,maphong,masddv,makt,madp;
	int tienhd; 
};
class nv{
	string manv,tennv; 
}; 
class hd{
	string mahd,manv,makh;
	int ngaynhap,tongtien; 
}; 
class khachhang{
	string makh,tenkhachhang
	int sdt; 
}; 
class loaiphong{
	string maphong,maloai,tenloai,gialoai; 
}; 
class phong{
	string maphong,loaiphong,maloai; 
}; 
class dp{
	string madp;
	int ngaytra,ngayhen,ngaynhan,phat; 
}; 
class sddv{
	string masddv,makh,manv;
	int tongtien; 
}; 
class sddvct{
	string masddv,madv;
	int soluong,tongtien; 
}; 
class dv{
	string madv,tendv;
	int tiendv; 
}; 
class kiemtraphong{
	string makt,maphong,tienden; 
}; 
class kiemtrachitiet{
	string makt,maphong,ttphong,matb;
	int tienden,soluonghong; 
}; 
class tbphong{
	string matb,tentb,maphong; 
}; 
class tb{
	string tentb,matb;
	int tienden; 
}; 
void dauhd{
	print(hd.mahd);
	tenkh=(hd.makh->kh.makh->tenkh) 
	print(nv.tennv);
	print(hd.ngaynhap);
}
void hdphong{
	int slphongdadat= count(hd.mahd->hdct.mahd,hdct.madp);
	print(slphongdadat);
	for(i::hdct){ 
		maphong=(hd.mahd->hdct.mahd,hdct.maphong);
		tenphong =(hd.mahd->hdct.mahd,hdct.maphong->phong.maphong,phong.maloai->loaiphong.maloai->loaiphong.tenloai);
		giaphong=(hd.mahd->hdct.mahd,hdct.maphong->phong.maphong,phong.maloai->loaiphong.maloai->loaiphong.gialoai) 
		print(maphong);
		print(loaiphong);
		print(giaphong);
		ngaydat=(hd.mahd->hdct.mahd,hdct.madp->dp.ngaydat);
		ngaytra=(hd.mahd->hdct.mahd,hdct.madp->dp.ngaytra);
		ngayhen=(hd.mahd->hdct.mahd,hdct.madp->dp.ngayhen);
		print(ngaydat);
		print(ngaytra);
		print(ngayhen);
		tienphong=giaphong*(ngaytra-ngaynhan); 
		tienphat=(hd.mahd->hdct.mahd,hdct.madp->dp.tienphat); 
		print(tienphong);
		print(tienphat);
		Tongtienphong = tienphong + tienphat 
		print(Tongtienphong); 
	} 
} 
void ktphong{
	denbu=(hd.mahd->hdct.mahd,hdct.makt->kiemtraphong.makt->kiemtraphong.tienden);
	if(denbu=0){
		print(denbu); 
	} 
	else if(denbu!=0){
		for(i::hdct){
			for(j::kiemtrachitiet){
				tenthietbi=(hd.makt->kiemtraphong.makt->kiemtrachitiet.makt->kiemtrachitiet.matb->tb.matb->tb.tentb);
				tienden=(hd.makt->kiemtraphong.makt->kiemtrachitiet.makt->kiemtrachitiet.matb->tb.matb->tb.tienden);
				soluonghong=(hd.makt->kiemtraphong.makt->kiemtrachitiet.makt->kiemtrachitiet.soluong)
				Tongtienden=tienden*soluonghong; 
				print(tenthietbi);  
				print(tienden);
				print(soluonghong); 
				print(Tongtienden); 
			} 
		} 
	} 	
} 
void sddv{
	for(i::hdct){
		for(j::sddvct){
			tendv=(hd.mahd->hdct.mahd,hdct.masddv->sddv.masddv->sddvct.masddv,sddvct.madv->dv.madv->dv.tendv);
			tiendv= (hd.mahd->hdct.mahd,hdct.masddv->sddv.masddv->sddvct.masddv,sddvct.madv->dv.madv->dv.giadv);
			soluongsddv=(hd.mahd->hdct.mahd,hdct.masddv->sddv.masddv->sddvct.masddv,sddvct.madv->sddvct.soluong);
			Tongtiensddv=soluongsddv*tiendv; 
			print(tendv);
			print(tiendv);
			print(soluongsddv); 
			print(Tongtiensddv); 
		} 
	} 
	soluongdvdadat=SUM(soluongsddv);
} 
void soluongdvdadat{
	soluongdvdadat=SUM(soluongsddv);
} 
int main(){
	dauhd;
	hdphong;
	ktphong;
	soluongdvdadat;
	sddv;
} 
